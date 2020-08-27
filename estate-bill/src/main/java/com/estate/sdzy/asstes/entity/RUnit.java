package com.estate.sdzy.asstes.entity;

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
 * 单元
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RUnit implements Serializable {

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

    /**
     * 社区id
     */
    private Long commId;

    /**
     * 区id
     */
    private Long commAreaId;

    /**
     * 状态
     */
    private String state;

    /**
     * 楼层数
     */
    private Integer floorNum;

    /**
     * 型号
     */
    private Long modelId;

    /**
     * 房间数
     */
    private Integer roomNum;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
    @TableLogic
    private Integer isDelete;

    /**
     * 楼宇id
     */
    private Long buildingId;

    @TableField(exist = false)
    private List<RRoom> childList;

    @TableField(exist = false)
    private Long oldBuildingId;
    @TableField(exist = false)
    private Long oldUnitId;

    @TableField(exist = false)
    private String compName;

    @TableField(exist = false)
    private String commName;

    @TableField(exist = false)
    private String areaName;

    @TableField(exist = false)
    private String buildingNo;

    @TableField(exist = false)
    private String buildingName;

    @TableField(exist = false)
    private String modelName;

    @TableField(exist = false)
    private String eleNum;

    @TableField(exist = false)
    private String rmNum;

}
