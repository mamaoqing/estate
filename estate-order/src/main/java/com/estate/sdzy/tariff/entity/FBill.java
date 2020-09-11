package com.estate.sdzy.tariff.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FBill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账单号
     */
    private String billNo;

    /**
     * 物业id：停车位、厂房、住宅的id
     */
    private Long propertyId;

    /**
     * 物业类型停车位、厂房、住宅
     */
    private String propertyType;

    /**
     * 账单生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date billTime;

    /**
     * 是否逾期
     */
    private String isOverdue;

    /**
     * 是否付款
     */
    private String isPayment;

    /**
     * 逾期产生的费用
     */
    private BigDecimal overdueCost;

    /**
     * 逾期费用计费规则
     */
    private String overdueRule;

    /**
     * 账单总价格
     */
    private BigDecimal price;

    /**
     * 已经付的钱
     */
        private BigDecimal payPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 费用减免
     */
    private BigDecimal salePrice;

    /**
     * 是否打印，打印过就不能打印了
     */
    private String isPrint;

    /**
     * 是否打印发票
     */
    private String isInvoice;

    /**
     * 收费结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payEndTime;

    /**
     * 费用标准id
     */
    private Long costRuleId;

    /**
     * 账期
     */
    private String accountPeriod;

    /**
     * 公司id
     */
    private Long compId;

    private Long commId;
    @TableField(exist = false)
    private String commName;

    private String beginScale;
    private String endScale;
    private String state;
    @TableField(exist = false)
    private String no;

    @TableField(exist = false)
    private String ruleName;
    private String createName;
    private Integer count;
}
