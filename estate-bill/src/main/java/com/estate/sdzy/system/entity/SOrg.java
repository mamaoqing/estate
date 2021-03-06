package com.estate.sdzy.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SOrg implements Serializable {

    public SOrg(Long companyId,String abbreviation,String remark){
        this.compId = companyId;
        this.name = abbreviation;
        this.remark = remark;
    }

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
     * 公司id
     */
    private Long compId;

    /**
     * 父机构id
     */
    private Long parentId;

    /**
     * 父机构id列表
     */
    private String parentIdList;

    /**
     * 状态
     */
    private String state;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

    private Long createdBy;

    private String createdName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifiedAt;

    private Long modifiedBy;

    private String modifiedName;
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private List<SOrg> childList;
    @TableField(exist = false)
    private String compName;

}
