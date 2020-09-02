package com.estate.sdzy.tariff.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    /**
     * 调整时间，发起时间
     */
    private Date alterTime;

    /**
     * 调整金额
     */
    private BigDecimal afterPrice;

    /**
     * 调整人
     */
    private String alterOwner;

    /**
     * 调整理由
     */
    private String alterReason;

    /**
     * 发起人
     */
    private String initiator;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核理由
     */
    private String audiReason;

    /**
     * 审核时间
     */
    private Date audiTime;

    /**
     * 审核是否通过
     */
    private String status;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 上调还是下降
     */
    private String alterType;


}
