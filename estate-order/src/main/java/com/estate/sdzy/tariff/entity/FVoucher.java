package com.estate.sdzy.tariff.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物业公司
     */
    private Long compId;

    /**
     * 物业公司名称
     */
    private String compName;

    /**
     * 社区名称
     */
    private String commName;

    /**
     * 业主名称
     */
    private String ownerName;

    /**
     * 社区id
     */
    private Long commId;

    /**
     * 编号
     */
    private Long no;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 业主id
     */
    private Long ownerId;

    private String createdName;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @TableField(exist = false)
    private String billIds;

}
