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
@TableName("tb_permission")
@ApiModel(value="TbPermission对象", description="")
@Alias("TbPermission")
@AllArgsConstructor
@NoArgsConstructor
public class TbPermission extends Model<TbPermission>  implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限")
    @TableField("permission_name")
    private String permissionName;

    @ApiModelProperty(value = "模块ID")
    @TableField("module_id")
    private Integer moduleId;

    @ApiModelProperty(value = "行为ID")
    @TableField("action_id")
    private Integer actionId;


}
