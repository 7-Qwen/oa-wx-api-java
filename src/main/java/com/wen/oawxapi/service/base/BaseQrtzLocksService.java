package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.QrtzLocks;
import com.wen.oawxapi.mapper.QrtzLocksMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseQrtzLocksService extends BaseService<QrtzLocksMapper, QrtzLocks>{

}
