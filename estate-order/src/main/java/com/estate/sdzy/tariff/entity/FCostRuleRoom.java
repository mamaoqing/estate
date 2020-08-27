package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class FCostRuleRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  费用标准id
     */
    private Long costRuleId;

    /**
     * 物业类型：停车位、厂房、住宅
     */
    private String propertyType;

    /**
     * 物业id
     */
    private Long propertyId;

    @TableLogic
    private Integer isDelete;


}
