package com.wen.oawxapi.vo.form;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: 7wen
 * @Date: 2023-06-25 17:29
 * @description: 用户签到提交表单
 */
@ApiModel(value = "签到表单", description = "签到表单")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckinForm {
    @ApiModelProperty(value = "签到地址")
    @NotBlank(message = "签到地址不能为空")
    private String address;

    @ApiModelProperty(value = "国家")
    @NotBlank(message = "签到国家不能为空")
    private String country;

    @ApiModelProperty(value = "省份")
    @NotBlank(message = "签到省份不能为空")
    private String province;

    @ApiModelProperty(value = "城市")
    @NotBlank(message = "签到城市不能为空")
    private String city;

    @ApiModelProperty(value = "区划")
    @NotBlank(message = "签到区域不能为空")
    private String district;
}
