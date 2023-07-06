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
@TableName("qrtz_cron_triggers")
@ApiModel(value="QrtzCronTriggers对象", description="")
@Alias("QrtzCronTriggers")
@AllArgsConstructor
@NoArgsConstructor
public class QrtzCronTriggers extends Model<QrtzCronTriggers>  implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "SCHED_NAME", type = IdType.ASSIGN_UUID)
    private String schedName;

    @TableField("TRIGGER_NAME")
    private String triggerName;

    @TableField("TRIGGER_GROUP")
    private String triggerGroup;

    @TableField("CRON_EXPRESSION")
    private String cronExpression;

    @TableField("TIME_ZONE_ID")
    private String timeZoneId;


}
