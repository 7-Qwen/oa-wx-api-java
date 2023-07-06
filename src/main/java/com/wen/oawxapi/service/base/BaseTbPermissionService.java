package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.TbPermission;
import com.wen.oawxapi.mapper.TbPermissionMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 *  服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbPermissionService extends BaseService<TbPermissionMapper, TbPermission>{

    @Resource
    private TbPermissionMapper tbPermissionMapper;
    /**
     * 根据用户id获取所有权限列表
     */
    public Set<String> getPermissionsByUserId(Long userId) {
        return tbPermissionMapper.getPermissionsByUserId(userId);
    }
}
