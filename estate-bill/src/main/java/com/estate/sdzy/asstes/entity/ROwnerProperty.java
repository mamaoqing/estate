package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 业主与物业对应关系
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ROwnerProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long ownerId;

    private Long compId;

    private Long commId;

    private Long commAreaId;

    /**
     * 物业类型：房产、停车位
     */
    private String propertyType;

    /**
     * 物业id:房产id、停车位id
     */
    private Long propertyId;

    private Long buildingId;

    private String type;

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
    @TableField(exist = false)
    private String compName;
    @TableField(exist = false)
    private String commName;
    @TableField(exist = false)
    private String commAreaName;
    @TableField(exist = false)
    private String buildingName;
}
