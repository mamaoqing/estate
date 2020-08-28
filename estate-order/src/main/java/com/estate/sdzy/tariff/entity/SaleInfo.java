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
public class SaleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long billId;

    /**
     * 减免的钱数
     */
    private BigDecimal salePrice;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 审批人
     */
    private String approver;

    private Date createTime;

    /**
     * 理由
     */
    private String reason;


}
