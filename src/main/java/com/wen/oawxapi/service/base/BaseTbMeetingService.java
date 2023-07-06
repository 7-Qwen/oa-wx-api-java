package com.wen.oawxapi.service.base;

import com.wen.oawxapi.entity.TbMeeting;
import com.wen.oawxapi.mapper.TbMeetingMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会议表 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbMeetingService extends BaseService<TbMeetingMapper, TbMeeting>{

}
