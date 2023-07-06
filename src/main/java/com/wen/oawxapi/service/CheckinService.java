package com.wen.oawxapi.service;

import com.wen.oawxapi.entity.TbCheckin;
import com.wen.oawxapi.entity.TbFaceModel;
import com.wen.oawxapi.vo.form.CheckinForm;

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
    void checkin(CheckinForm checkinForm,Long userId,String path);


    /**
     * 创建用户人脸模型
     */
    void createUserModel(Long userId, String path);
}
