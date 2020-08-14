package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 社区分区表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RCommArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 物业公司
     */
    private Long compId;

    /**
     * 小区id
     */
    private Long commId;

    /**
     * 详细地址
     */
    private String detailedAddress;

    /**
     * 建造日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" ,timezone = "GMT+8")
    private Date buildedDate;

    /**
     * 交付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" ,timezone = "GMT+8")
    private Date deliverDate;

    /**
     * 状态
     */
    private String state;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 社区图标
     */
    private String iconPath;

    /**
     * 地图地址
     */
    private String mapAddress;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" ,timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" ,timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private String commName;

    @TableField(exist = false)
    private String compName;

    @TableField(exist = false)
    private List<RBuilding> childList;

}
