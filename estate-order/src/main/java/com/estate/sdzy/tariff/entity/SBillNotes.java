package com.estate.sdzy.tariff.entity;

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
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SBillNotes implements Serializable {

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
     * 社区id
     */
    private Long commId;

    /**
     * 单据备注信息
     */
    private String notes;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建人名称
     */
    private String createdName;


}
