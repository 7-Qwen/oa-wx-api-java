package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.TbModule;
import com.wen.oawxapi.mapper.TbModuleMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 模块资源表 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbModuleService extends BaseService<TbModuleMapper, TbModule>{

}
