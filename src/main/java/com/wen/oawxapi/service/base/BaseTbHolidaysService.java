package com.wen.oawxapi.service.base;

import cn.hutool.core.util.StrUtil;
import com.wen.oawxapi.common.exception.CustomException;
import com.wen.oawxapi.entity.TbHolidays;
import com.wen.oawxapi.mapper.TbHolidaysMapper;
import com.wen.oawxapi.common.mybatisplus.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 节假日表 服务实现类
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseTbHolidaysService extends BaseService<TbHolidaysMapper, TbHolidays> {

    /**
     * 查询放假日期
     */
    public ArrayList<String> searchHolidaysDate(HashMap<String, Object> param) {
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        if (StrUtil.isBlank(startTime) || StrUtil.isBlank(endTime)) {
            throw new CustomException("参数不能为空");
        }
        List<TbHolidays> tbHolidays = this.lambdaQuery()
                .select(TbHolidays::getDate)
                .between(TbHolidays::getDate, startTime, endTime)
                .list();
        ArrayList<String> date = new ArrayList<>();
        tbHolidays.forEach(h -> {
            date.add(h.getDate().toString());
        });
        return date;
    }
}
