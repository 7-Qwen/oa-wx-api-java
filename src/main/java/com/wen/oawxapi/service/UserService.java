package com.wen.oawxapi.service;

import com.wen.oawxapi.entity.TbUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * @author: 7wen
 * @Date: 2023-06-06 15:20
 * @description:
 */
@Service
public interface UserService {
    /**
     * 注册用户
     *
     * @param registerCode 注册码
     * @param code         js获取临时认证Code
     * @param nickName     用户昵称
     * @param photo        头像
     * @return boolean
     */
    TbUser registeredUsers(String registerCode, String code, String nickName, String photo);


    /**
     * 用户登录
     */
    TbUser login(String jsCode);


    /**
     * 根据用户id获取用户权限
     *
     * @param userId 用户id
     * @return 用户权限去重集合
     * @author 7wen
     * @date 2023-06-07 11:51
     */
    Set<String> getUserPermission(Long userId);


    /**
     * 获取用户信息
     */
    Map<String, String> getUserInfo(Long userId);
}
