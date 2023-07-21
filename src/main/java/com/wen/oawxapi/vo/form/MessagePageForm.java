package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: 7wen
 * @Date: 2023-07-21 16:59
 * @description:
 */
@ApiModel(value = "分页消息表单vo")
@Data
public class MessagePageForm {
    @NotNull
    @Min(1)
    private Long page;
    @NotNull
    @Range(min = 1, max = 40)
    private Long size;
}
