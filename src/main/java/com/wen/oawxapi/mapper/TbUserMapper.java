package com.wen.oawxapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.oawxapi.entity.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 用户表 Mapper 接口
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Mapper
public interface TbUserMapper extends BaseMapper<TbUser> {

    /**
     * 获取用户信息
     */
    Map<String, String> getUserInfo(Long userId);
}
