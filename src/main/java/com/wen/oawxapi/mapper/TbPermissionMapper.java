package com.wen.oawxapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wen.oawxapi.entity.TbPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 *  Mapper 接口
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Mapper
public interface TbPermissionMapper extends BaseMapper<TbPermission> {

    Set<String> getPermissionsByUserId(@Param("userId") Long userId);
}
