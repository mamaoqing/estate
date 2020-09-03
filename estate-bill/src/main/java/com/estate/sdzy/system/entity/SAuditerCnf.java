package com.estate.sdzy.system.entity;

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
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SAuditerCnf implements Serializable {

    private static final long serialVersionUID = -6457231180611325450L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long compId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "物业公司",master = true)
    private String compName;

    private Long commId;
    @TableField(exist = false)
    @ExcelAnnotation(value = "社区",master = true)
    private String commName;

    /**
     * 审核人类型
     */
    private String type;

    /**
     * 用户id
     */
    private Long userId;
    @TableField(exist = false)
    private String name;

    /**
     * 排序号
     */
    private int orderNo;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
}
