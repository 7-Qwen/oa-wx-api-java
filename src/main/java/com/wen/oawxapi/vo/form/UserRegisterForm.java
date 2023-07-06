package com.wen.oawxapi.vo.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author: 7wen
 * @Date: 2023-06-06 15:23
 * @description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户注册表单", description = "用户注册表单")
public class UserRegisterForm {

    /**
     * 注册码
     */
    @ApiModelProperty(value = "注册码")
    @NotBlank(message = "注册码不能为空")
    @Pattern(regexp = "^[0-9]{6}$", message = "注册码必须为6位数字")
    private String registerCode;

    /**
     * js获取的临时授权码
     */
    @ApiModelProperty(value = "js获取的临时授权码")
    @NotBlank(message = "微信临时授权码不能为空")
    private String code;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    @NotBlank(message = "用户头像不能为空")
    private String photo;
}
