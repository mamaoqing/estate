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
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PayInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 付款人
     */
    private String payer;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 实际付款金额
     */
    private BigDecimal payPrice;

    /**
     * 付款方式：现金，微信，支付宝，其他
     */
    private String payType;


}
