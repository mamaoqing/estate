package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.estate.sdzy.common.annotation.ExcelAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 停车位
 * </p>
 *
 * @author mq
 * @since 2020-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RParkingSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ExcelAnnotation(value = "编号",master = true)
    private String no;

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
    @ExcelAnnotation(value = "社区名",master = true)
    private String commName;

    /**
     * 区id
     */
    private Long commAreaId;

    @TableField(exist = false)
    @ExcelAnnotation(value = "分区名称",master = true)
    private String areaName;

    /**
     * 位置
     */
    @ExcelAnnotation(value = "位置")
    private String position;

    /**
     * 建筑属性
     */
    @ExcelAnnotation(value = "建筑属性",dist = "34")
    private String buildingProperty;

    /**
     * 使用属性
     */
    @ExcelAnnotation(value = "使用属性",dist = "35")
    private String useProperty;

    /**
     * 车辆类型
     */
    @ExcelAnnotation(value = "车辆类型",dist = "36")
    private String vehicleType;

    /**
     * 高度
     */
    @ExcelAnnotation(value = "高度",dist = "38")
    private String height;

    /**
     * 尺寸
     */
    @ExcelAnnotation(value = "尺寸",dist = "37")
    private String size;

    /**
     * 排列形式
     */
    @ExcelAnnotation(value = "排列形式",dist = "39")
    private String array;

    /**
     * 可用时间
     */
    @ExcelAnnotation(value = "可用时间",dist = "40")
    private String usableTime;

    /**
     * 占用状态
     */
    @ExcelAnnotation(value = "占用状态",dist = "41")
    private String occupyState;

    /**
     * 入位方式
     */
    @ExcelAnnotation(value = "入位方式",dist = "42")
    private String inMode;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;

    @TableLogic
    private Integer isDelete;


}
