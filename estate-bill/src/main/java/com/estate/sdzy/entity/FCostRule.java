package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 收费标准
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FCostRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long costTypeId;

    /**
     * 费用项目id
     */
    private Long costItemId;

    /**
     * 费用规则名称
     */
    private String name;

    private Long compId;

    /**
     * 开始日期
     */
    private Date beginDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 状态
     */
    private String state;

    /**
     * 计费方式
     */
    private String billingMethod;

    /**
     * 价格类型
     */
    private String priceType;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格单位
     */
    private BigDecimal priceUnit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 是否有违约金
     */
    private Integer isLiquidatedDamages;

    /**
     * 违约金计算方式
     */
    private String liquidatedDamagesMethod;

    /**
     * 账单周期
     */
    private String billCycle;

    /**
     * 出账天
     */
    private String billDay;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;


}
