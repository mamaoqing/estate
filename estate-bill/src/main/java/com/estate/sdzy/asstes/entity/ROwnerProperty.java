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
    private Long ownerId;
    private Long compId;
    private Long commId;
    private Long commAreaId;
    private Long buildingId;

    @TableField(exist = false)
    @ExcelAnnotation(value = "物业公司",master = true)
    private String compName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "社区",master = true)
    private String commName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "分区",master = true)
    private String commAreaName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "建筑",master = true)
    private String buildingName;
    /**
     * 物业类型：房产、停车位
     */
    @ExcelAnnotation(value = "物业类型",master = true,dist = "45")
    private String propertyType;

    /**
     * 物业id:房产id、停车位id
     */
    private Long propertyId;

    @ExcelAnnotation(value = "物业编号",master = true)
    @TableField(exist = false)
    private String propertyName;


    @TableField(exist = false)
    @ExcelAnnotation(value = "证件类型",dist = "47")
    private String certType;
    @TableField(exist = false)
    @ExcelAnnotation(value = "证件号码",master = true)
    private String certNumber;

    @TableField(exist = false)
    @ExcelAnnotation(value = "业主名称",master = true)
    private String ownerName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "业主类型",dist = "12",master = true)
    private String ownerType;
    @TableField(exist = false)
    @ExcelAnnotation(value = "业主地址" ,export = false)
    private String ownerAddr;
    @TableField(exist = false)
    @ExcelAnnotation(value = "电话",export = false)
    private String tel;
    @TableField(exist = false)
    @ExcelAnnotation(value = "邮箱",export = false)
    private String eMail;

    @TableField(exist = false)
    @ExcelAnnotation(value = "行业",export = false)
    private String industry;
    @TableField(exist = false)
    @ExcelAnnotation(value = "性别",export = false)
    private String sex;
    @TableField(exist = false)
    @ExcelAnnotation(value = "籍贯",export = false)
    private String nativePlace;
    @TableField(exist = false)
    @ExcelAnnotation(value = "学历",export = false)
    private String education;
    @TableField(exist = false)
    @ExcelAnnotation(value = "联系人",export = false)
    private String linkName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "联系人电话",export = false)
    private String linkTel;
    @TableField(exist = false)
    @ExcelAnnotation(value = "联系人地址",export = false)
    private String linkAddr;


    @ExcelAnnotation(value = "房屋关系类型",master = true,dist = "11")
    private String type;

    /**
     * 备注
     */
    @ExcelAnnotation(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "创建时间",master = true,export = false)
    private Date createdAt;

    private Long createdBy;
    @ExcelAnnotation(value = "创建人",master = true,export = false)
    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "修改时间",master = true,export = false)
    private Date modifiedAt;

    private Long modifiedBy;
    @ExcelAnnotation(value = "修改人",master = true,export = false)
    private String modifiedName;
    @TableLogic
    private Integer isDelete;




}
