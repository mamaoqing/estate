package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 仪表表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FMeter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;
    @TableField(exist = false)
    private String compName;

    private Long commId;
    @TableField(exist = false)
    private String commName;
    /**
     * 物业类型：房产、停车位
     */
    private String propertyType;

    /**
     * 物业id：房产id、停车位id
     */
    private Long propertyId;
    @TableField(exist = false)
    private String propertyName;

    /**
     * 仪表类型（水表、电表、煤气表）
     */
    private String type;
    /**
     * 仪表编号
     */
    private String no;

    /**
     * 当前数量
     */
    private BigDecimal newNum;
    /**
     * 抄表时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date meterReadTime;

    /**
     * 前次数量（生成账单）
     */
    private BigDecimal billNum;
    /**
     * 账单日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date billDate;

    /**
     * 状态
     */
    private String state;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;

    private Integer isDelete;
}
