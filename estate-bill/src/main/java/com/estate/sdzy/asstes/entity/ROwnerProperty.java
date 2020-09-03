package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
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

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ExcelAnnotation(value = "业主",master = true)
    private Long ownerId;
    @ExcelAnnotation(value = "公司",master = true)
    private Long compId;
    @ExcelAnnotation(value = "社区",master = true)
    private Long commId;
    @ExcelAnnotation(value = "分区",master = true)
    private Long commAreaId;

    /**
     * 物业类型：房产、停车位
     */
    @ExcelAnnotation(value = "物业类型",master = true)
    private String propertyType;

    /**
     * 物业id:房产id、停车位id
     */
    @ExcelAnnotation(value = "物业",master = true)
    private Long propertyId;
    @ExcelAnnotation(value = "楼栋",master = true)
    private Long buildingId;
    @ExcelAnnotation(value = "房屋关系类型",master = true)
    private String type;

    /**
     * 备注
     */
    @ExcelAnnotation(value = "证件号码")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "创建时间",master = true)
    private Date createdAt;

    private Long createdBy;
    @ExcelAnnotation(value = "创建人",master = true)
    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "修改时间",master = true)
    private Date modifiedAt;

    private Long modifiedBy;
    @ExcelAnnotation(value = "修改人",master = true)
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

    @TableField(exist = false)
    private String ownerName;
    @TableField(exist = false)
    private String ownerType;
    @TableField(exist = false)
    private String ownerAddr;
    @TableField(exist = false)
    private String tel;
    @TableField(exist = false)
    private String eMail;
    @TableField(exist = false)
    private String certType;
    @TableField(exist = false)
    private String certNumber;
    @TableField(exist = false)
    private String industry;
    @TableField(exist = false)
    private String sex;
    @TableField(exist = false)
    private String nativePlace;
    @TableField(exist = false)
    private String education;
    @TableField(exist = false)
    private String linkName;
    @TableField(exist = false)
    private String linkTel;
    @TableField(exist = false)
    private String linkAddr;

}
