package com.wen.oawxapi.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.common.utils.JwtUtils;
import com.wen.oawxapi.common.utils.R;
import com.wen.oawxapi.entity.TbUser;
import com.wen.oawxapi.service.CheckinService;
import com.wen.oawxapi.service.UserService;
import com.wen.oawxapi.vo.back.UserBackVo;
import com.wen.oawxapi.vo.form.CheckinForm;
import com.wen.oawxapi.vo.form.LoginForm;
import com.wen.oawxapi.vo.form.UserRegisterForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户web层
 *
 * @author: 7wen
 * @date: 2023-06-06 19:44
 * @description: 用户相关模块 登录注册 + 签到
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping(value = "/user")
@CrossOrigin
@Slf4j
public class UserController {
    @Value("${oa.jwt.cache-expire}")
    private Integer cacheExpire;
    @Resource
    private OaConfig oaConfig;
    @Resource
    private UserService userService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private CheckinService checkinService;


    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public R register(@RequestBody @Valid UserRegisterForm userRegisterForm) {
        //执行用户注册
        TbUser tbUser = userService.registeredUsers(
                userRegisterForm.getRegisterCode(),
                userRegisterForm.getCode(),
                userRegisterForm.getNickName(),
                userRegisterForm.getPhoto());
        if (tbUser == null) {
            return R.error("注册失败");
        }
        //获取token
        String token = jwtUtils.createToken(tbUser.getId());
        //将token存储在redis中
        saveCacheToken(tbUser.getId().toString(), token);
        //todo 获取用户的角色权限组
        return R.ok("注册成功!").put("token", token).put("permisson", userService.getUserPermission(tbUser.getId()));
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public R login(@RequestBody @Valid LoginForm loginForm) {
        TbUser user = userService.login(loginForm.getCode());
        //获取用户权限
        Set<String> userPermission = userService.getUserPermission(user.getId());
        //创建token
        String token = jwtUtils.createToken(user.getId());
        //缓存token
        saveCacheToken(user.getId().toString(), token);
        //转换user 脱敏不必要的返回值
        UserBackVo userBackVo = BeanUtil.toBean(user, UserBackVo.class);
        return R.ok("登录成功").put("token", token).put("permission", userPermission).put("user", userBackVo);
    }

    @ApiOperation(value = "查询用户是否可以签到")
    @GetMapping("/checkCheckin")
    @RequiresPermissions(value = {"ROOT", "USER:READ"}, logical = Logical.OR)
    public R checkCheckin(@RequestHeader(value = "token") String token) {
        Long userId = jwtUtils.getUserId(token);
        return R.ok(checkinService.checkCheckin(userId));
    }

    @ApiOperation(value = "用户签到")
    @PostMapping("/check")
    public R check(@Valid CheckinForm checkinForm, @RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        if (file == null) {
            return R.error("没有上传文件");
        }
        Long userId = jwtUtils.getUserId(token);
        String path = oaConfig.getImageFolder() + "/" + file.getOriginalFilename().toLowerCase();
        if (!path.endsWith("jpg") && !path.endsWith("jpeg")) {
            throw new CustomException("文件只支持上传jpg或者jpeg图片");
        } else {
            try {
                //将内存中的file文件持久化
                file.transferTo(Paths.get(path));
                checkinService.checkin(checkinForm, userId, path);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtil.del(path);
            }
        }
        return R.ok("签到成功");
    }


    @ApiOperation(value = "用户创建模型")
    @PostMapping("/check/createModel")
    public R createModel(@RequestParam("photo") MultipartFile file, @RequestHeader("token") String token) {
        //校验文件
        if (file == null) {
            return R.error("文件为空");
        }
        Long userId = jwtUtils.getUserId(token);
        String filename = file.getOriginalFilename().toLowerCase();
        String path = oaConfig.getImageFolder() + "/" + filename;
        if (!filename.endsWith(".jpg") && !filename.endsWith(".jpeg")) {
            throw new CustomException("文件只支持上传jpg或者jpeg图片");
        }
        //完成文件本地化
        try {
            file.transferTo(Paths.get(path));
            checkinService.createUserModel(userId, path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("文件保存出错了喔");
        } finally {
            FileUtil.del(path);
        }
        return R.ok("创建模型成功");
    }

    /**
     * 保存token至缓存中
     */
    private void saveCacheToken(String uid, String token) {
        redisTemplate.opsForValue().set(token, uid, cacheExpire, TimeUnit.DAYS);
    }
}
