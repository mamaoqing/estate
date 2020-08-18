package com.estate.sdzy.asstes.entity;

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
    @TableField(exist = false)
    private String compName;

    /**
     * 社区id
     */
    private Long commId;
    @TableField(exist = false)
    private String commName;

    /**
     * 区id
     */
    private Long commAreaId;
    @TableField(exist = false)
    private String commAreaName;

    /**
     * 单元id
     */
    private Long unitId;
    @TableField(exist = false)
    private String unitName;

    /**
     * 状态
     */
    private String state;

    /**
     * 楼层
     */
    private Integer floor;
    /**
     * 楼层数
     */
    @TableField(exist = false)
    private Integer floorNum;
    /**
     * 电梯数
     */
    @TableField(exist = false)
    private Integer elevatorNum;
    /**
     * 房间数
     */
    @TableField(exist = false)
    private Integer roomNum;

    /**
     * 房型
     */
    private String roomModel;
    @TableField(exist = false)
    private String roomModelName;
    /**
     * 房屋类型
     */
    private String roomType;
    @TableField(exist = false)
    private String roomTypeName;
    /**
     * 产权性质
     */
    private String propertyRightNature;
    @TableField(exist = false)
    private String propertyRightNatureName;
    /**
     * 朝向
     */
    private String direction;
    @TableField(exist = false)
    private String directionName;
    /**
     * 装修程度
     */
    private String renovationLevel;
    @TableField(exist = false)
    private String renovationLevelName;
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
    @TableField(exist = false)
    private String usableName;

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
    //@TableLogic
    private Integer isDelete;

    /**
     * 楼宇id
     */
    private Long buildingId;
    @TableField(exist = false)
    private String buildingName;


}
