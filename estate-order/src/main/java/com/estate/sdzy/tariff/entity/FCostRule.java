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
 * 收费标准
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FCostRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

        private Long costTypeId;
    @TableField(exist = false)
    private String costTypeName;

    /**
     * 物业类型
     */
    @TableField(exist = false)
    private String propertyType;
    /**
     * 物业id
     */
    @TableField(exist = false)
    private String propertyId;
    /**
     * 物业名称
     */
    @TableField(exist = false)
    private String propertyName;

    /**
     * 费用项目id
     */
    private Long costItemId;
    @TableField(exist = false)
    private String costItemName;

    /**
     * 费用规则名称
     */
    private String name;

    private Long compId;
    @TableField(exist = false)
    private String compName;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 状态
     */
    private String state;

    /**
     * 计费方式
     */
    private String billingMethod;

    /**
     * 价格类型
     */
    private String priceType;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格单位
     */
    private String priceUnit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 是否有违约金
     */
    private String isLiquidatedDamages;

    /**
     * 违约金计算方式
     */
    private String liquidatedDamagesMethod;

    /**
     * 账单周期
     */
    private String billCycle;

    /**
     * 出账天
     */
    private Integer billDay;

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

    private Integer payTime;

    private String audit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    private Long commId;
}
