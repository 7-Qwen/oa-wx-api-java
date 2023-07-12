package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author: 7wen
 * @Date: 2023-07-12 23:52
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询月份签到表单",description = "查询月份签到表单")
public class MonthCheckinForm {
    @NotNull
    @Range(min = 2000,max = 3000)
    private Integer month;
    @NotNull
    @Range(min = 1, max = 12)
    private Integer year;
}
