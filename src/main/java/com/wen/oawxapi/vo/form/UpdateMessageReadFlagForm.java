package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 7wen
 * @Date: 2023-07-21 17:13
 * @description:
 */
@Data
@ApiModel(value = "更新消息未读状态提交表单")
public class UpdateMessageReadFlagForm {
    @NotBlank
    private String id;
}
