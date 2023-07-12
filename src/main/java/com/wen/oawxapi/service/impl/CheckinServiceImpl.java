package com.wen.oawxapi.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.wen.oawxapi.common.config.ymlArgementConfig.oa.OaConfig;
import com.wen.oawxapi.common.environment.SystemConstant;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.entity.*;
import com.wen.oawxapi.mapper.TbCheckinMapper;
import com.wen.oawxapi.mapper.TbUserMapper;
import com.wen.oawxapi.service.CheckinService;
import com.wen.oawxapi.service.base.*;
import com.wen.oawxapi.task.EmailTask;
import com.wen.oawxapi.vo.form.CheckinForm;
import com.wen.oawxapi.vo.third.faceIdentify.FaceResponse;
import com.wen.oawxapi.vo.third.faceIdentify.FaceResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-06-25 16:51
 * @description: 签到服务
 */
@Service
@Slf4j
public class CheckinServiceImpl extends Throwable implements CheckinService {
    @Resource
    private BaseTbWorkdayService baseTbWorkdayService;
    @Resource
    private BaseTbHolidaysService baseTbHolidaysService;
    @Resource
    private BaseTbCheckinService baseTbCheckinService;
    @Resource
    private BaseTbFaceModelService baseTbFaceModelService;
    @Resource
    private BaseTbUserService baseTbUserService;
    @Resource
    private TbUserMapper tbUserMapper;
    @Resource
    private SystemConstant systemConstant;
    @Resource
    private OaConfig oaConfig;
    @Resource
    private EmailTask emailTask;
    @Resource
    private TbCheckinMapper tbCheckinMapper;


    @Override
    public Boolean searchTodayIsWorkDay() {
        return baseTbWorkdayService.lambdaQuery()
                .eq(TbWorkday::getDate, DateUtil.format(DateUtil.date(), "yyyy-MM-dd"))
                .count() > 0;
    }

    @Override
    public Boolean searchTodayIsHoliday() {
        return baseTbHolidaysService.lambdaQuery()
                .eq(TbHolidays::getDate, DateUtil.date())
                .count() > 0;
    }


    /**
     * 校验用户是否已经签到过
     */
    public Boolean searchUserIsCheckin(Long userId) {
        return baseTbCheckinService.lambdaQuery()
                .eq(TbCheckin::getUserId, userId)
                .eq(TbCheckin::getDate, DateUtil.format(DateUtil.date(), "yyyy-MM-dd"))
                .count() > 0;
    }


    /**
     * 检查是否可以签到
     */
    @Override
    public String checkCheckin(Long userId) {
        //获取当前时间
        DateTime now = DateUtil.date();
        //结合假期以及公司内部制定的工作休息日确定变量
        Boolean isWorkDay = this.searchTodayIsWorkDay();
        Boolean isHoliday = this.searchTodayIsHoliday();
        String type = "工作日";
        if (now.isWeekend()) {
            type = "休息日";
        }
        if (isWorkDay) {
            type = "工作日";
        } else if (isHoliday) {
            type = "休息日";
        }

        //开始检查是否需要签到
        if (type.equals("休息日")) {
            return "今日为休息日无需签到";
        } else {
            //获取上班签到开始时间
            DateTime attendanceStartTime = DateUtil.parse(systemConstant.getAttendanceStartTime());
            //获取上班签到结束时间
            DateTime attendanceEndTime = DateUtil.parse(systemConstant.getAttendanceEndTime());

            //开始校验签到时间
            if (now.isBefore(attendanceStartTime)) {
                return "还没有到签到时间";
            } else if (now.isAfter(attendanceEndTime)) {
                return "已过签到时间,无法签到";
            }
            //校验用户是否重复签到
            return this.searchUserIsCheckin(userId) ? "用户已签到,无需重复考勤" : "用户可以进行签到";
        }
    }


    /**
     * 用户是否建模
     */
    @Override
    public TbFaceModel userIsModel(Long userId) {
        return baseTbFaceModelService.lambdaQuery()
                .eq(TbFaceModel::getUserId, userId)
                .one();
    }


    /**
     * 插入用户模型
     */
    @Override
    public void insertUserModel(Long userId, String faceModel) {
        if (!new TbFaceModel(null, userId, faceModel).insert()) {
            throw new CustomException("新建用户模型失败");
        }
    }


    /**
     * 删除用户模型
     */
    @Override
    public void deleteUserModel(Long userId) {
        if (!baseTbFaceModelService.lambdaUpdate().eq(TbFaceModel::getUserId, userId).remove()) {
            throw new CustomException("删除用户模型失败");
        }
    }

    /**
     * 用户进行签到
     * checkinForm 签到表单
     * userId 用户id
     * path 上传文件地址
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void checkin(CheckinForm checkinForm, Long userId, String path) {
        String resCheck = this.checkCheckin(userId);
        if (!"用户可以进行签到".equals(resCheck)) {
            throw new CustomException(resCheck);
        }
        //初始化参数status
        int status = 0;
        //校验时间
        DateTime now = DateUtil.date();
        //上班时间
        DateTime attendDanceTime = DateUtil.parse(DateUtil.today() + " " + systemConstant.getAttendanceTime());
        //上班考勤结束时间
        DateTime attendDanceEndTime = DateUtil.parse(DateUtil.today() + " " + systemConstant.getAttendanceEndTime());

        //如果当前时间早于上班时间
        if (now.compareTo(attendDanceTime) <= 0) {
            //正常考勤
            status = 1;
            //如果当前时间晚于上班时间且早于上班打卡时间
        } else if (now.compareTo(attendDanceTime) > 0 && now.compareTo(attendDanceEndTime) < 0) {
            //迟到
            status = 2;
        }

        //查询该用户的人脸模型
        TbFaceModel tbFaceModel = this.userIsModel(userId);
        if (tbFaceModel == null) {
            throw new CustomException("用户未建模");
        }

        //人脸校验
        HttpRequest post = HttpUtil.createPost(oaConfig.getFace().getIdentifyUrl());
        HttpResponse httpResponse = post.form("file", FileUtil.file(path), "modelId", tbFaceModel.getFaceModel()).execute();
        //todo 检验参数500的情况
        //转换json
        JSON response = JSONUtil.parse(httpResponse.body());
        //通过远程调用获取接口返回值
        FaceResponse result = response.toBean(FaceResponse.class);
        FaceResult faceResult = result.getResult();
        String message = result.getMessage();
        //如果返回值不等于200 则说明出现异常,返回异常
        if (faceResult.getCode() != HttpStatus.SC_OK) {
            log.error("人脸识别服务器出现问题,异常信息:" + message);
            throw new CustomException(message);
        } else {
            //说明认证成功,获取结果二次校验
            if (!faceResult.getIsSuccess()) {
                log.error("code=200,但是isSuccess返回false");
                throw new CustomException("返回参数异常");
            }
        }

        //获取未离职用户信息
        TbUser user = baseTbUserService.lambdaQuery()
                .eq(TbUser::getId, userId)
                .eq(TbUser::getStatus, 1)
                .one();

        if (user == null) {
            throw new CustomException("用户不存在或者已离职");
        }

        //发送邮箱
        Map<String, String> userInfo = tbUserMapper.getUserInfo(userId);
        String name = StrUtil.isBlank(userInfo.get("name")) ? "未知姓名" : userInfo.get("name");
        String deptName = StrUtil.isBlank(userInfo.get("deptName")) ? "暂无部门" : userInfo.get("deptName");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(
                oaConfig.getEmail().getHr(),
                oaConfig.getEmail().getBoss());
        mail.setSubject("员工:" + name + "进行了签到");
        mail.setText(
                deptName + "的员工" + name + ",于"
                        + DateUtil.format(new Date(), "yyyy年MM月dd日 HH:mm:ss")
                        + "在"
                        + checkinForm.getAddress() + "进行了签到,请检查。"
        );
        emailTask.sendAsync(mail);

        //保存签到信息
        TbCheckin tbCheckin = BeanUtil.copyProperties(checkinForm, TbCheckin.class);
        tbCheckin.setUserId(userId);
        tbCheckin.setStatus(status);
        //现已无疫情
        tbCheckin.setRisk(0);
        tbCheckin.setDate(LocalDate.now());
        tbCheckin.setCreateTime(LocalDateTime.now());
        if (!tbCheckin.insert()) {
            throw new CustomException("签到插入用户数据失败");
        }
    }


    /**
     * 创建用户人脸模型
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void createUserModel(Long userId, String path) {
        //创建远程调用
        HttpRequest post = HttpUtil.createPost(oaConfig.getFace().getModelUrl());
        HttpResponse response = post.form("file", FileUtil.file(path)).execute();
        //获取结果
        FaceResponse faceResponse = JSONUtil.parse(response.body()).toBean(FaceResponse.class);
        if (faceResponse.getResult().getCode() != HttpStatus.SC_OK) {
            log.error("远程调用人脸识别接口出错,信息:" + faceResponse.getMessage());
            throw new CustomException(faceResponse.getMessage());
        } else {
            //获取modelId
            Long modelId = faceResponse.getModelId();
            //存储face信息
            TbFaceModel tbFaceModel = new TbFaceModel();
            System.out.println(userId);
            tbFaceModel.setUserId(userId);
            tbFaceModel.setFaceModel(modelId.toString());
            if (!tbFaceModel.insert()) {
                throw new CustomException("存储用户模型错误");
            }
        }
    }

    /**
     * 查询用户今日签到情况
     */
    @Override
    public HashMap<String, Object> searchUserCheckinToday(Long userId) {
        return tbCheckinMapper.searchUserCheckinToday(userId);
    }

    /**
     * 查询用户总签到数
     */
    @Override
    public Integer totalUserCheckin(Long userId) {
        return baseTbCheckinService.searchHowManyUserCheckin(userId);
    }

    /**
     * 查询用户一周的签到情况
     */
    @Override
    public ArrayList<HashMap<String, Object>> searchWeekCheckin(HashMap<String, Object> param) {
        //根据时间参数查询用户在时间段内签到情况
        ArrayList<HashMap<String, String>> checkinsList = tbCheckinMapper.searchUserCheckinsByTime(param);
        //获取时间段内的假期日期
        ArrayList<String> holidaysDate = baseTbHolidaysService.searchHolidaysDate(param);
        //获取时间段内的工作日期
        ArrayList<String> workDaysByTime = baseTbWorkdayService.searchWorkDaysByTime(param);
        DateTime startTime = DateUtil.parse((String) param.get("startTime"));
        DateTime endTime = DateUtil.parse((String) param.get("endTime"));
        //获取从开始时间到结束时间的所有日期范围
        DateRange range = DateUtil.range(startTime, endTime, DateField.DAY_OF_MONTH);
        //声明容器
        ArrayList<HashMap<String, Object>> list = new ArrayList();
        range.forEach(rangeDate -> {
            //获取日期
            String date = rangeDate.toString("yyyy-MM-dd");
            //校验当前日期的工作类型:是否为工作日,如果不在特殊的休息日和工作日的话就是正常的工作日
            String type = "工作日";
            if (holidaysDate.contains(date)) {
                type = "节假日";
            } else if (workDaysByTime.contains(date)) {
                type = "工作日";
            }
            //校验签到状态
            String status = "";
            //如果工作类型为工作日且日期在今天或今天之前
            if ("工作日".equals(type) && rangeDate.isBeforeOrEquals(DateUtil.date())) {
                //当前日期用户是否签到标记
                boolean flag = false;
                for (HashMap<String, String> userCheckDate : checkinsList) {
                    //如果用户在当前日期签到了直接返回其签到状态
                    if (userCheckDate.containsValue(date)) {
                        status = userCheckDate.get("status");
                        flag = true;
                        break;
                    }
                    //定义签到结束时间
                    DateTime attendEndTime = DateUtil.parse(DateUtil.date() + systemConstant.attendanceEndTime);
                    //如果用户没有在今天有签到记录,则检验是否为签到结束时间前还未签到的情况
                    if (date.equals(DateUtil.today()) && DateUtil.date().isBefore(attendEndTime) && !flag) {
                        status = "";
                    }
                }
            }
            //如果类型为假期则无需用户签到情况
            HashMap<String, Object> hashMap = new HashMap();
            hashMap.put("date", date);
            hashMap.put("status", status);
            hashMap.put("type", type);
            hashMap.put("day", rangeDate.dayOfWeekEnum().toChinese("周"));
            //完成当前rangeDate的状态
            list.add(hashMap);
        });
        return list;
    }


    /**
     * 查询用户一月的签到情况
     */
    @Override
    public ArrayList<HashMap<String, Object>> searchMonthCheckin(HashMap<String, Object> param) {
        return this.searchWeekCheckin(param);
    }
}
