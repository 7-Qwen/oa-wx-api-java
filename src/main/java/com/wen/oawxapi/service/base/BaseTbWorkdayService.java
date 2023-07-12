package com.wen.oawxapi.service.base;

import cn.hutool.core.util.StrUtil;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.entity.TbWorkday;
import com.wen.oawxapi.mapper.TbWorkdayMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbWorkdayService extends BaseService<TbWorkdayMapper, TbWorkday> {

    /**
     * 根据时间获取工作date
     */
    public ArrayList<String> searchWorkDaysByTime(HashMap<String, Object> param) {
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        if (StrUtil.isBlank(startTime) || StrUtil.isBlank(endTime)) {
            throw new CustomException("参数不能为空");
        }
        List<TbWorkday> workdays = this.lambdaQuery()
                .select(TbWorkday::getDate)
                .between(TbWorkday::getDate, startTime, endTime)
                .list();
        ArrayList<String> list = new ArrayList<>();
        workdays.forEach(tbWorkday -> {
            list.add(tbWorkday.getDate().toString());
        });
        return list;
    }
}
