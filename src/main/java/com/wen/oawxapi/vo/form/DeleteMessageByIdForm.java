package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 7wen
 * @Date: 2023-07-21 17:15
 * @description:
 */
@Data
@ApiModel("删除消息提交表单")
public class DeleteMessageByIdForm {
    @NotBlank
    private String id;
}
