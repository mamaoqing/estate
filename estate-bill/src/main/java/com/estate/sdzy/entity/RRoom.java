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
 * 房间
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 房间号
     */
    private String roomNo;

    /**
     * 物业公司
     */
    private Long compId;

    /**
     * 社区id
     */
    private Long commId;

    /**
     * 区id
     */
    private Long commAreaId;

    /**
     * 单元id
     */
    private Long unitId;

    /**
     * 状态
     */
    private String state;

    /**
     * 楼层数
     */
    private Integer floor;

    /**
     * 房型
     */
    private String roomModel;

    /**
     * 产权证号
     */
    private String titleDeedNo;

    /**
     * 土地证号
     */
    private String landDeedNo;

    /**
     * 购房合同号
     */
    private String contractNo;

    /**
     * 建筑面积
     */
    private BigDecimal buildingArea;

    /**
     * 使用面积
     */
    private BigDecimal usableArea;

    /**
     * 花园面积
     */
    private BigDecimal gardenArea;

    /**
     * 用途
     */
    private String usable;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
    @TableLogic
    private Integer isDelete;

    /**
     * 楼宇id
     */
    private Long buildingId;


}
