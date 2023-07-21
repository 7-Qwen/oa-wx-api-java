package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 7wen
 * @Date: 2023-07-21 17:08
 * @description:
 */
@Data
@ApiModel(value = "通过id获取消息的表单")
public class MessageIdForm {
    @NotBlank
    private String id;
}
