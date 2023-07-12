package com.wen.oawxapi.service;

import com.wen.oawxapi.entity.TbCheckin;
import com.wen.oawxapi.entity.TbFaceModel;
import com.wen.oawxapi.vo.form.CheckinForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-06-25 16:37
 * @description:
 */
public interface CheckinService {

    /**
     * 检查今天是否为工作日
     */
    Boolean searchTodayIsWorkDay();


    /**
     * 检查今天是否为假期
     */
    Boolean searchTodayIsHoliday();


    /**
     * 检查是否可以签到
     */
    String checkCheckin(Long userId);


    /**
     * 检查用户是否建模
     */
    TbFaceModel userIsModel(Long userId);


    /**
     * 插入用户模型
     */
    void insertUserModel(Long userId, String faceModel);


    /**
     * 删除用户模型
     */
    void deleteUserModel(Long userId);

    /**
     * 签到
     */
    void checkin(CheckinForm checkinForm, Long userId, String path);


    /**
     * 创建用户人脸模型
     */
    void createUserModel(Long userId, String path);


    /**
     * 查询用户今日签到情况
     */
    HashMap<String, Object> searchUserCheckinToday(Long userId);

    /**
     * 查询用户总签到数
     */
    Integer totalUserCheckin(Long userId);

    /**
     * 查询用户一周的签到情况
     */
    ArrayList<HashMap<String, Object>> searchWeekCheckin(HashMap<String, Object> param);


    /**
     * 查询用户一周的签到情况
     */
    ArrayList<HashMap<String, Object>> searchMonthCheckin(HashMap<String, Object> param);


}

