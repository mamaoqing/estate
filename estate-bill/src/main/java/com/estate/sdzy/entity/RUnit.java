package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单元
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RUnit implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 社区id
     */
    private Long commId;

    /**
     * 区id
     */
    private Long commAreaId;

    /**
     * 楼栋id
     */
    private Long buildingId;

    /**
     * 状态
     */
    private String state;

    /**
     * 楼层数
     */
    private Integer floorNum;

    /**
     * 型号
     */
    private String model;

    /**
     * 房间数
     */
    private Integer roomNum;

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

    @TableField(exist = false)
    private List<RRoom> roomList;

}
