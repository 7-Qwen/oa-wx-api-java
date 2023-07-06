package com.wen.oawxapi.entity;
//needUpdate

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 签到表
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_checkin")
@ApiModel(value="TbCheckin对象", description="签到表")
@Alias("TbCheckin")
@AllArgsConstructor
@NoArgsConstructor
public class TbCheckin extends Model<TbCheckin>  implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "签到地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "国家")
    @TableField("country")
    private String country;

    @ApiModelProperty(value = "省份")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "区划")
    @TableField("district")
    private String district;

    @ApiModelProperty(value = "考勤结果")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "风险等级")
    @TableField("risk")
    private Integer risk;

    @ApiModelProperty(value = "签到日期")
    @TableField("date")
    private LocalDate date;

    @ApiModelProperty(value = "签到时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
