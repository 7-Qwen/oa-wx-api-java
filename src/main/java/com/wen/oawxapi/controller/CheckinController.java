package com.wen.oawxapi.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.environment.SystemConstant;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.common.utils.JwtUtils;
import com.wen.oawxapi.common.utils.R;
import com.wen.oawxapi.vo.form.CheckinForm;
import com.wen.oawxapi.vo.form.MonthCheckinForm;
import com.wen.oawxapi.entity.TbUser;
import com.wen.oawxapi.service.CheckinService;
import com.wen.oawxapi.service.base.BaseTbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author: 7wen
 * @Date: 2023-07-12 23:23
 * @description:
 */
@Api(tags = "签到web")
@RestController
@CrossOrigin
@RequestMapping("/checkin")
public class CheckinController {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private BaseTbUserService baseTbUserService;
    @Resource
    private CheckinService checkinService;
    @Resource
    private SystemConstant systemConstant;
    @Resource
    private OaConfig oaConfig;

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


    @ApiOperation("获取用户周签到数据")
    @GetMapping("/searchThisWeekUserCheckin")
    public R searchThisWeekUserCheckin(@RequestHeader("token") String token) {
        //通过token获取用户信息
        Long userId = jwtUtils.getUserId(token);
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getId, userId)
                .eq(TbUser::getStatus, 1)
                .one();
        if (user == null) {
            throw new CustomException("用户数据为空,请联系管理员");
        }
        HashMap<String, Object> map = checkinService.searchUserCheckinToday(userId);
        //加入上下班时间参数
        map.put("attendanceTime", systemConstant.attendanceTime);
        map.put("closingTime", systemConstant.closingTime);
        //加入签到总天数参数
        map.put("checkinDays", checkinService.totalUserCheckin(userId));
        //获取用户入职时间
        LocalDate hiredate = user.getHiredate();
        //转换时间
        DateTime hiredateFormat = DateUtil.parse(hiredate.toString());
        DateTime startTime = DateUtil.beginOfWeek(DateUtil.date());
        //校验当前时间的周的第一天是否在入职日期前,如果是则以入职时间为开始时间
        if (startTime.isBefore(hiredateFormat)) {
            startTime = hiredateFormat;
        }
        DateTime endTime = DateUtil.endOfWeek(DateUtil.date());
        //声明参数
        HashMap<String, Object> paramHashMap = new HashMap(4);
        paramHashMap.put("startTime", startTime.toString());
        paramHashMap.put("endTime", endTime.toString());
        paramHashMap.put("userId", userId);
        ArrayList<HashMap<String, Object>> list = checkinService.searchWeekCheckin(paramHashMap);
        map.put("weekCheckin", list);
        return R.ok().put("result", map);
    }

    @ApiOperation(value = "获取用户月签到数据")
    @PostMapping("/getUserMonthCheckin")
    public R getUserMonthCheckin(@Valid @RequestBody MonthCheckinForm monthCheckinForm, @RequestHeader("token") String token) {
        //获取用户id
        Long userId = jwtUtils.getUserId(token);
        //获取用户信息
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getId, userId)
                .eq(TbUser::getStatus, 1)
                .one();
        if (user == null) {
            throw new CustomException("用户数据为空,请联系管理员");
        }
        //处理双数字
        String month = monthCheckinForm.getMonth() < 10 ? "0" + monthCheckinForm.getMonth() : "" + monthCheckinForm.getMonth();
        //处理起始时间 查询年-查询月-第一天
        DateTime startTime = DateUtil.parse(monthCheckinForm.getYear() + "-" + month + "-01");
        //获取用户入职日期
        DateTime hiredate = DateUtil.parse(user.getHiredate().toString());
        //如果查询的时间早于用户入职日期当月范围则报错
        if (startTime.isBefore(DateUtil.beginOfMonth(hiredate))) {
            throw new CustomException("当前选择月份用户还未入职");
        }
        //如果查询时间等于用户入职日期则开始时间=用户入职日期
        if (startTime.isBefore(hiredate)) {
            startTime = hiredate;
        }
        DateTime endTime = DateUtil.endOfMonth(startTime);
        //声明入参
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("startTime", startTime.toString());
        map.put("endTime", endTime.toString());
        map.put("userId", userId);
        ArrayList<HashMap<String, Object>> monthCheckin = checkinService.searchMonthCheckin(map);
        //计算status参数额情况
        //正常
        int sum_1 = 0;
        //迟到
        int sum_2 = 0;
        //缺勤
        int sum_3 = 0;
        for (HashMap<String, Object> checkin : monthCheckin) {
            String type = (String) checkin.get("type");
            String status = (String) checkin.get("status");
            if ("工作日".equals(type)) {
                if ("正常".equals(status)) {
                    sum_1++;
                } else if ("迟到".equals(status)) {
                    sum_2++;
                } else if ("缺勤".equals(status)) {
                    sum_3++;
                }
            }
        }
        return R.ok().put("monthCheckin", monthCheckin).put("sum_1", sum_1).put("sum_2", sum_2).put("sum_3", sum_3);
    }
}
