package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 业主表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ROwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主类型
     */
    @ExcelAnnotation(value = "业主类型",master = true,dist = "12")
    private String ownerType;

    /**
     * 物业公司
     */
    private Long compId;

    /**
     * 证件类型
     */
    @ExcelAnnotation(value = "证件类型",dist = "47")
    private String certType;

    /**
     * 证件号码
     */
    @ExcelAnnotation(value = "证件号码",master = true)
    private String certNumber;

    /**
     * 业主名称
     */
    @ExcelAnnotation(value = "业主名称",master = true)
    private String name;

    /**
     * 业主地址
     */
    @ExcelAnnotation(value = "业主地址",master = true)
    private String ownerAddr;

    /**
     * 电话
     */
    @ExcelAnnotation(value = "业主电话",master = true)
    private String tel;

    /**
     * 邮箱
     */
    @ExcelAnnotation(value = "业主邮箱")
    private String eMail;

    private String wxOpenid;

    private String wxUnionid;

    private String wxNickname;

    private String wxSex;

    private String wxProvince;

    private String wxCity;

    private String wxCountry;



    /**
     * 籍贯
     */
    @ExcelAnnotation(value = "籍贯",master = true)
    private String nativePlace;

    /**
     * 行业
     */
    @ExcelAnnotation(value = "行业",master = true,dist = "49")
    private String industry;

    /**
     * 性别
     */
    @ExcelAnnotation(value = "性别",master = true,dist = "14")
    private String sex;


    /**
     * 学历
     */
    @ExcelAnnotation(value = "学历",master = true)
    private String education;

    /**
     * 爱好
     */
    private String likes;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 状态
     */
    @ExcelAnnotation(value = "状态",master = true)
    private String state;

    /**
     * 联系人
     */
    @ExcelAnnotation(value = "联系人")
    private String linkName;

    /**
     * 联系人电话
     */
    @ExcelAnnotation(value = "联系人电话")
    private String linkTel;

    /**
     * 联系人地址
     */
    @ExcelAnnotation(value = "联系人地址")
    private String linkAddr;

    /**
     * 备注
     */
    @ExcelAnnotation(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "录入时间")
    private Date createdAt;

    private Long createdBy;
    @ExcelAnnotation(value = "录入人")
    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "修改时间")
    private Date modifiedAt;

    private Long modifiedBy;
    @ExcelAnnotation(value = "修改人")
    private String modifiedName;
    @TableField(exist = false)
    @ExcelAnnotation(value = "物业公司",master = true)
    private String compName;

    @TableField(exist = false)
    private Long commId;
    @TableField(exist = false)
    private Long commAreaId;
    @TableField(exist = false)
    private Long buildingId;
    @TableField(exist = false)
    private Long roomId;
    @TableField(exist = false)
    private String propTypes;
    @TableField(exist = false)
    private Long propId;

}
