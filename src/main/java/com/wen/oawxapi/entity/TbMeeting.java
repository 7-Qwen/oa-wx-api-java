package com.wen.oawxapi.entity;
//needUpdate

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalTime;
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
 * 会议表
 *
 * @author 7wen
 * @since 2023-06-06
 */
@Data
@Accessors(chain = true)
@TableName("tb_meeting")
@ApiModel(value="TbMeeting对象", description="会议表")
@Alias("TbMeeting")
@AllArgsConstructor
@NoArgsConstructor
public class TbMeeting extends Model<TbMeeting>  implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "UUID")
    @TableField("uuid")
    private String uuid;

    @ApiModelProperty(value = "会议题目")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "创建人ID")
    @TableField("creator_id")
    private Long creatorId;

    @ApiModelProperty(value = "日期")
    @TableField("date")
    private LocalDate date;

    @ApiModelProperty(value = "开会地点")
    @TableField("place")
    private String place;

    @ApiModelProperty(value = "开始时间")
    @TableField("start")
    private LocalTime start;

    @ApiModelProperty(value = "结束时间")
    @TableField("end")
    private LocalTime end;

    @ApiModelProperty(value = "会议类型（1在线会议，2线下会议）")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "参与者")
    @TableField("members")
    private String members;

    @ApiModelProperty(value = "会议内容")
    @TableField("desc")
    private String desc;

    @ApiModelProperty(value = "工作流实例ID")
    @TableField("instance_id")
    private String instanceId;

    @ApiModelProperty(value = "状态（1未开始，2进行中，3已结束）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
