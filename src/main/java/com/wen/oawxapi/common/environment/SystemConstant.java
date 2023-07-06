package com.wen.oawxapi.common.environment;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author: 7wen
 * @Date: 2023-06-20 21:12
 * @description: 系统字典
 */
@Data
@Component
public class SystemConstant {
    public String attendanceStartTime;
    public String attendanceTime;
    public String attendanceEndTime;
    public String closingStartTime;
    public String closingTime;
    public String closingEndTime;
}
