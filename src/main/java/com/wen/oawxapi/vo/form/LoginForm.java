package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author: 7wen
 * @Date: 2023-06-09 10:21
 * @description:
 */
@ApiModel(value = "登录表单", description = "登录表单")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @ApiModelProperty(value = "微信临时校验码")
    @NotBlank(message = "微信临时校验码不能为空")
    private String code;
}
