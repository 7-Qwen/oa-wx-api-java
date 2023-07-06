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
 * 模块资源表
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_module")
@ApiModel(value="TbModule对象", description="模块资源表")
@Alias("TbModule")
@AllArgsConstructor
@NoArgsConstructor
public class TbModule extends Model<TbModule>  implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模块编号")
    @TableField("module_code")
    private String moduleCode;

    @ApiModelProperty(value = "模块名称")
    @TableField("module_name")
    private String moduleName;


}
