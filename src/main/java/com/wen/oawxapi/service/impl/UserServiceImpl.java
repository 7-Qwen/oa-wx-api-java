package com.wen.oawxapi.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wen.oawxapi.common.environment.CommonConstantPaper;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.entity.TbUser;
import com.wen.oawxapi.service.base.BaseTbPermissionService;
import com.wen.oawxapi.service.base.BaseTbRoleService;
import com.wen.oawxapi.service.base.BaseTbUserService;
import com.wen.oawxapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @author: 7wen
 * @Date: 2023-06-06 15:20
 * @description:
 */
@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${oa.jwt.expire}")
    private Integer expire;
    @Value("${oa.wx.appId}")
    private String appId;
    @Value("${oa.wx.secret}")
    private String secret;
    String url = "https://api.weixin.qq.com/sns/jscode2session";
    @Resource
    private BaseTbUserService baseTbUserService;
    @Resource
    private BaseTbRoleService baseTbRoleService;
    @Resource
    private BaseTbPermissionService baseTbPermissionService;


    /**
     * 注册用户
     */
    @Override
    public TbUser registeredUsers(String registerCode, String code, String nickName, String photo) {
        //声明tbUser,如果注册失败则会抛出异常
        TbUser tbUser = null;
        if (registerCode.equals(CommonConstantPaper.ADMIN_REGISTER)) {
            //先检查用户表是否含有超级管理员
            if (this.isExistAdmin()) {
                throw new CustomException("该系统已存在管理员");
            }
            //如果管理员未注册则开始注册管理员流程
            tbUser = new TbUser();
            //1.获取该用户的openId
            String userOpenId = this.getUserOpenId(code);
            tbUser.setOpenId(userOpenId);
            tbUser.setNickname(nickName);
            tbUser.setPhoto(photo);
            tbUser.setHiredate(LocalDateTimeUtil.now().toLocalDate());
            tbUser.setCreateTime(LocalDateTimeUtil.now());
            tbUser.setRole("[0]");
            tbUser.setRoot(true);
            tbUser.setStatus(1);
            //插入用户
            boolean res = tbUser.insertOrUpdate();
            if (!res) {
                throw new CustomException("新建用户失败,请联系管理员");
            }
        } else {
            //todo 待补充
        }
        return tbUser;
    }


    /**
     * 用户登录
     */
    @Override
    public TbUser login(String jsCode) {
        //通过jsCode获取用户openId
        String userOpenId = getUserOpenId(jsCode);
        //通过数据库查询openId是否存在,如果存在则表示登录成功
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getOpenId, userOpenId)
                .one();
        if (user == null) {
            throw new CustomException("用户不存在,请前往注册");
        }
        return user;
    }


    /**
     * 获取用户权限
     */
    @Override
    public Set<String> getUserPermission(Long userId) {
        //todo 获取用户权限
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getId, userId)
                .one();
        if (user == null) {
            throw new CustomException("用户不存在");
        }
        return baseTbPermissionService.getPermissionsByUserId(user.getId());
    }

    /**
     * 校验是否存在管理员
     */
    public Boolean isExistAdmin() {
        return baseTbUserService.lambdaQuery()
                .eq(TbUser::getRoot, true)
                .one() != null;
    }

    /**
     * 获取微信用户的openId
     */
    public String getUserOpenId(@NotBlank String jsCode) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("appid", appId);
        map.put("secret", secret);
        map.put("js_code", jsCode);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        String openid = jsonObject.getStr("openid");
        if (StrUtil.isBlank(openid)) {
            throw new RuntimeException("获取用户openId异常");
        }
        return openid;
    }

}
