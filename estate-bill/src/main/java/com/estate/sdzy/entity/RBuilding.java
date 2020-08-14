package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 楼房
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RBuilding implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String no;
    /**
     * 物业公司
     */
    private Long compId;
    @TableField(exist = false)
    private String compName;
    /**
     * 社区id
     */
    private Long commId;
    @TableField(exist = false)
    private String commName;
    /**
     * 区id
     */
    private Long commAreaId;
    @TableField(exist = false)
    private String commAreaName;
    /**
     * 建造日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date buildedDate;

    /**
     * 交付日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date deliverDate;

    /**
     * 状态
     */
    private String state;

    /**
     * 楼层数
     */
    private Integer floorNum;

    /**
     * 单元数
     */
    private Integer unitNum;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
    @TableLogic
    private Integer isDelete;

    /**
     * 建筑类型
     */
    private String type;
    @TableField(exist = false)
    private String dictName;

    @TableField(exist = false)
    private List<RUnit> childList;

}
