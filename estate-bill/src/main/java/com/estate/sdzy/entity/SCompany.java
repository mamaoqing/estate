package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 物业公司表
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 简称
     */
    private String abbreviation;

    /**
     * 成立日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date establishmentDate;

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
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 县区
     */
    private String district;

    /**
     * 公司地址
     */
    private String compAddr;

    /**
     * 注册地址
     */
    private String registeredAddr;

    /**
     * 注册资本
     */
    private String registeredCapital;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 纳税人识别号
     */
    private String taxpayerIdentificationNo;

    /**
     * 工商注册号
     */
    private String registeredNo;

    /**
     * 公司类型
     */
    private String compType;

    /**
     * 营业期限开始
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date businessTermBegin;

    /**
     * 营业期限结束
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date businessTermEnd;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 状态
     */
    private String state;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;

    private Integer isDelete;


}
