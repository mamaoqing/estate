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
 * 
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FFinanceBillRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;

    private Long commId;

    private Long financeRecordId;

    private Long billId;

    private BigDecimal cost;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    private Long createdBy;

    private String createdName;


}
