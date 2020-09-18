package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账户
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司id
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
     * 费用项目
     */
    @TableField(exist = false)
    private String ruleName;

    /**
     * 业主id
     */
    private Long ownerId;

    /**
     * 费用标准id
     */
    @TableField(exist = false)
    private String ruleId;

    @TableField(exist = false)
    private String propertyName;
    @TableField(exist = false)
    private String propertyId;
    @TableField(exist = false)
    private String propertyType;

    /**
     * 账户名称
     */
    private String name;
    @TableField(exist = false)
    private String paymentMethod;
    /**
     * 账号编号
     */
    private String no;
    @TableField(exist = false)
    private String billNo;
    /**
     * 账号余额
     */
    private BigDecimal fee;
    @TableField(exist = false)
    private BigDecimal feeAdd;
    private String type;
    /**
     * 业主名称
     */
    @TableField(exist = false)
    private String ownerName;
    /**
     * 业主联系电话
     */
    @TableField(exist = false)
    private String ownerTel;

    /**
     * 是否续费 true -续费
     */
    @TableField(exist = false)
    private Boolean isReNew;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
}
