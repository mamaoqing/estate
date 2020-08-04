package com.estate.sdzy.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仪表流水表
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FMeterRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;

    private Long commId;

    private Long meterId;

    /**
     * 物业类型：房产、停车位
     */
    private String propertyType;

    /**
     * 物业id：房产id、停车位id
     */
    private Long propertyId;

    /**
     * 仪表类型（水表、电表、煤气表）
     */
    private String type;

    /**
     * 操作类型（抄表、出账单、缴费）
     */
    private String operType;

    /**
     * 当前数量
     */
    private BigDecimal newNum;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    private Long createdBy;

    private String createdName;


}
