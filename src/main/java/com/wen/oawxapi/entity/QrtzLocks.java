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
@TableName("qrtz_locks")
@ApiModel(value="QrtzLocks对象", description="")
@Alias("QrtzLocks")
@AllArgsConstructor
@NoArgsConstructor
public class QrtzLocks extends Model<QrtzLocks>  implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "SCHED_NAME", type = IdType.ASSIGN_UUID)
    private String schedName;

    @TableField("LOCK_NAME")
    private String lockName;


}
