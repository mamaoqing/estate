package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 物业公司
     */
    private Long compId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "物业公司",master = true)
    private String compName;

    /**
     * 社区id
     */
    private Long commId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "社区",master = true)
    private String commName;

    /**
     * 区id
     */
    private Long commAreaId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "分区",master = true)
    private String commAreaName;
    /**
     * 楼宇id
     */
    private Long buildingId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "建筑",master = true)
    private String buildingName;
    @TableField(exist = false)
    private String buildingNo;
    /**
     * 单元id
     */
    private Long unitId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "单元",master = true)
    private String unitName;
    @TableField(exist = false)
    private String unitNo;
    /**
     * 名称
     */
    @ExcelAnnotation(value = "房间名称",master = true)
    private String name;

    /**
     * 房间号
     */
    @ExcelAnnotation(value = "房间号",master = true)
    private String roomNo;


    /**
     * 楼层
     */
    @ExcelAnnotation(value = "楼层")
    private Integer floor;
    /**
     * 楼层数
     */
    @ExcelAnnotation(value = "楼层数")
    @TableField(exist = false)
    private Integer floorNum;
    /**
     * 电梯数
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "电梯数")
    private Integer elevatorNum;
    /**
     * 房间数
     */
    @TableField(exist = false)
    @ExcelAnnotation(value = "每层房间数")
    private Integer roomNum;

    /**
     * 房型
     */
    @ExcelAnnotation(value = "房型",dist = "44")
    private String roomModel;
    /**
     * 房屋类型
     */
    @ExcelAnnotation(value = "房屋类型",dist = "1")
    private String roomType;

    /**
     * 产权性质
     */
    @ExcelAnnotation(value = "产权性质",dist = "27")
    private String propertyRightNature;

    /**
     * 朝向
     */
    @ExcelAnnotation(value = "朝向",dist = "30")
    private String direction;
    /**
     * 装修程度
     */
    @ExcelAnnotation(value = "装修程度",dist = "31")
    private String renovationLevel;
    /**
     * 用途
     */
    @ExcelAnnotation(value = "用途",dist = "7")
    private String usable;

    /**
     * 产权证号
     */
    @ExcelAnnotation(value = "产权证号")
    private String titleDeedNo;

    /**
     * 土地证号
     */
    @ExcelAnnotation(value = "土地证号")
    private String landDeedNo;

    /**
     * 购房合同号
     */
    @ExcelAnnotation(value = "购房合同号")
    private String contractNo;

    /**
     * 建筑面积
     */
    @ExcelAnnotation(value = "建筑面积",master = true)
    private BigDecimal buildingArea;

    /**
     * 使用面积
     */
    @ExcelAnnotation(value = "使用面积",master = true)
    private BigDecimal usableArea;
    /**
     * 暖气计费面积
     */
    @ExcelAnnotation(value = "暖气计费面积",master = true)
    private BigDecimal heatingArea;
    /**
     * 花园面积
     */
    @ExcelAnnotation(value = "花园面积")
    private BigDecimal gardenArea;

    /**
     * 状态
     */
    @ExcelAnnotation(value = "状态",master = true)
    private String state;

    /**
     * 备注
     */
    @ExcelAnnotation(value = "备注")
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "录入时间",fmt="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    private Long createdBy;
    @ExcelAnnotation(value = "录入人")
    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ExcelAnnotation(value = "修改时间",fmt="yyyy-MM-dd HH:mm:ss")
    private Date modifiedAt;

    private Long modifiedBy;
    @ExcelAnnotation(value = "修改人")
    private String modifiedName;
    //@TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private String no;




}
