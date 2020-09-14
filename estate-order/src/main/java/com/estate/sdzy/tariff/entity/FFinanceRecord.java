package com.estate.sdzy.tariff.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 财务流水
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FFinanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;

    private Long commId;

    /**
     * 流水单号
     */
    private String no;

    private Long accountId;

    /**
     * 操作类型（支付，预存，取现）
     */
    private String operType;

    /**
     * 金额
     */
    private BigDecimal cost;

    private Long ownerId;

    /**
     * 支付方式（微信、支付宝、现金、银行转账等）
     */
    private String paymentMethod;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    private Long createdBy;

    private String createdName;


}
