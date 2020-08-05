package com.estate.sdzy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 业主表
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ROwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主类型
     */
    private String ownerType;

    /**
     * 业主名称
     */
    private String name;

    /**
     * 业主地址
     */
    private String ownerAddr;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 证件类型
     */
    private String certType;

    private String wxOpenid;

    private String wxUnionid;

    private String wxNickname;

    private String wxSex;

    private String wxProvince;

    private String wxCity;

    private String wxCountry;

    /**
     * 证件号码
     */
    private String certNumber;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 学历
     */
    private String education;

    /**
     * 爱好
     */
    private String likes;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 状态
     */
    private String state;

    /**
     * 联系人
     */
    private String linkName;

    /**
     * 联系人电话
     */
    private String linkTel;

    /**
     * 联系人地址
     */
    private String linkAddr;

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
