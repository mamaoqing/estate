package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 社区表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RCommunity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 小区名称
     */
    @ExcelAnnotation(value = "小区名称",master = true)
    private String name;

    /**
     * 物业公司
     */
    private Long compId;

    @ExcelAnnotation(value = "物业公司",master = true)
    @TableField(exist = false)
    private String compName;

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 县区
     */
    private Long districtId;

    /**
     * 省
     */
    @ExcelAnnotation(value = "省",master = true)
    private String province;

    /**
     * 市
     */
    @ExcelAnnotation(value = "市")
    private String city;

    /**
     * 县区
     */
    @ExcelAnnotation(value = "县区")
    private String district;

    /**
     * 详细地址
     */
    @ExcelAnnotation(value = "详细地址")
    private String detailedAddress;

    /**
     * 建造日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private Date buildedDate;

    /**
     * 交付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ExcelAnnotation(value = "交付日期")
    private Date deliverDate;

    /**
     * 用途类型
     */
    @ExcelAnnotation(value = "用途类型")
    private String usableType;

    /**
     * 状态
     */
    private String state;

    /**
     * 电话
     */
    @ExcelAnnotation(value = "tel")
    private String tel;

    /**
     * 邮箱
     */
    @ExcelAnnotation(value = "邮箱")
    private String eMail;

    /**
     * 社区简介/介绍
     */
    private String introduction;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 社区图标
     */
    private String iconPath;

    /**
     * 地图地址
     */
    private String mapAddress;

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
    private List<RCommArea> childList;


}
