package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FBillAlter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "物业公司")
    private String compName;

    private Long commId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "社区")
    private String commName;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 物业类型：房产、停车位
     */
    @ExcelAnnotation(value = "物业类型")
    @TableField(exist = false)
    private String propertyType;

    /**
     * 物业id
     */
    @TableField(exist = false)
    private Long property_id;

    @TableField(exist = false)
    @ExcelAnnotation(value = "物业编号")
    private String propertyName;

    /**
     * 费用标准
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "费用标准")
    private String costRuleName;

    /**
     * 账期
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "账期")
    private String accountPeriod;

    /**
     * 费用金额
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "费用金额")
    private BigDecimal price;

    @TableField(exist = false)
    private BigDecimal payPrice;//已经付的钱
    @TableField(exist = false)
    private BigDecimal salePrice;//费用调整
    @TableField(exist = false)
    private BigDecimal overdueCost;//逾期产生的费用
    /**
     * 账单状态
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "账单状态")
    private String billState;

    /**
     * 状态
     */
    @ExcelAnnotation(value = "调整状态")
    private String state;
    /**
     * 调整金额
     */
    @ExcelAnnotation(value = "调整金额")
    private BigDecimal alterFee;

    /**
     * 调整原因
     */
    @ExcelAnnotation(value = "调整原因")
    private String alterReason;

    /**
     * 调整人id
     */
    private Long alterBy;
    @TableField(exist = false)
    @ExcelAnnotation(value = "调整人")
    private String alterByName;

    /**
     * 调整时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "调整时间")
    private Date alterTime;

    /**
     * 审核意见
     */
    @ExcelAnnotation(value = "审核意见")
    private String audiReason;
    /**
     * 审核人
     */
    private Long auditor;
    @TableField(exist = false)
    @ExcelAnnotation(value = "审核人")
    private String auditorName;
    @TableField(exist = false)
    private String userName;//审核人用户名

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "审核时间")
    private Date auditTime;



    /**
     * 备注
     */
    @ExcelAnnotation(value = "备注")
    private String remark;
}
