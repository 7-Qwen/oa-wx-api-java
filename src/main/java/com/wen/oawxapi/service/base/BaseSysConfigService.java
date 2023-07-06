package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.SysConfig;
import com.wen.oawxapi.mapper.SysConfigMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseSysConfigService extends BaseService<SysConfigMapper, SysConfig> {

    /**
     * 获取所有字典数据
     */
    public List<SysConfig> getAllConfigValue() {
        return this.lambdaQuery().list();
    }
}
