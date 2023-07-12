package com.wen.oawxapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.oawxapi.entity.TbCheckin;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.crypto.hash.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 签到表 Mapper 接口
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Mapper
public interface TbCheckinMapper extends BaseMapper<TbCheckin> {

    /**
     * 查询用户今日签到情况
     */

    HashMap<String, Object> searchUserCheckinToday(@Param("userId") Long userId);


    /**
     * 根据开始结束时间获取用户签到情况
     */
    ArrayList<HashMap<String, String>> searchUserCheckinsByTime(HashMap param);
}