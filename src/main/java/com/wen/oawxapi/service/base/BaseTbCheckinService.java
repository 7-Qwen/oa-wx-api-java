package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.TbCheckin;
import com.wen.oawxapi.mapper.TbCheckinMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 签到表 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbCheckinService extends BaseService<TbCheckinMapper, TbCheckin>{

    /**
     * 查询用户总签到数
     */
    public Integer searchHowManyUserCheckin(Long userId) {
        return this.lambdaQuery().eq(TbCheckin::getUserId, userId).count();
    }


}
