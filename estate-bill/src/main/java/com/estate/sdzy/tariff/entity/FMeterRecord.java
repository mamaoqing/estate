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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 仪表流水表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FMeterRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;
    @TableField(exist = false)
    private String compName;

    private Long commId;
    @TableField(exist = false)
    private String commName;

    private Long meterId;

    /**
     * 物业类型：房产、停车位
     */
    private String propertyType;
    /**
     * 物业编号
     */
    @TableField(exist = false)
    private String propertyName;

    /**
     * 物业id：房产id、停车位id
     */
    private Long propertyId;

    /**
     * 仪表类型（水表、电表、煤气表）
     */
    private String type;
    /**
     * 仪表编号
     */
    @TableField(exist = false)
    private String no;
    /**
     * 仪表名称
     */
    private String name;

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

    //@TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedAt;

    private Long modifiedBy;
    @ExcelAnnotation(value = "修改人")
    private String modifiedName;

    private Integer isDelete;

}
