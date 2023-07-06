package com.wen.oawxapi.entity;
//needUpdate

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Data
@Accessors(chain = true)
@TableName("sys_config")
@ApiModel(value="SysConfig对象", description="")
@Alias("SysConfig")
@AllArgsConstructor
@NoArgsConstructor
public class SysConfig extends Model<SysConfig>  implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "参数名")
    @TableField("param_key")
    private String paramKey;

    @ApiModelProperty(value = "参数值")
    @TableField("param_value")
    private String paramValue;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


}
