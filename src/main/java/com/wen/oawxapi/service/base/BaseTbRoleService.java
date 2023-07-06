package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.TbRole;
import com.wen.oawxapi.mapper.TbRoleMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色表 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbRoleService extends BaseService<TbRoleMapper, TbRole>{

}
