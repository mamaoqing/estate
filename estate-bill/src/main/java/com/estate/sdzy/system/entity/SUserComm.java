package com.estate.sdzy.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户社区表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SUserComm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 构建实例
     * @param compId 公司id
     * @param userId 需要设置权限的用户id
     * @param commId 社区id
     * @param remark 备注
     */
    public SUserComm(Long compId, Long userId, Long commId, String remark,Long createdBy,String createdName) {
        this.compId =compId;
        this.commId = commId;
        this.userId = userId;
        this.commId = commId;
        this.remark = remark;
        this.createdBy = createdBy;
        this.createdName = createdName;
    }

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司id
     */
    private Long compId;

    /**
     * 用户id
     */
    private Long userId;


    /**
     * 社区id
     */
    private Long commId;

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


}
