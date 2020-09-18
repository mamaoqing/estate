package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 账户消费标准
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FAccountCostItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司id
     */
    private Long compId;

    /**
     * 社区id
     */
    private Long commId;

    /**
     * 账户id
     */
    private Long accountId;

    /**
     * 费用标准id
     */
    private Long ruleId;

    /**
     * 物业类型
     */
    private String propertyType;
    /**
     * 费用项
     */
    @TableField(exist = false)
    private String name;
    /**
     * 物业编号
     */
    @TableField(exist = false)
    private String propertyName;

    /**
     * 物业id
     */
    private Long propertyId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Long createdBy;

    private String createdName;
}
