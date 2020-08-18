package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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

    private String no;

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
     * 位置
     */
    private String position;

    /**
     * 建筑属性
     */
    private String buildingProperty;

    /**
     * 使用属性
     */
    private String useProperty;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 高度
     */
    private String height;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 排列形式
     */
    private String array;

    /**
     * 可用时间
     */
    private String usableTime;

    /**
     * 占用状态
     */
    private String occupyState;

    /**
     * 入位方式
     */
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
