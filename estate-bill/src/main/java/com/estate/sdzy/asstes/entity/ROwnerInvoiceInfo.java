package com.estate.sdzy.asstes.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 业主开票信息
 * </p>
 *
 * @author mq
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ROwnerInvoiceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long ownerId;

    /**
     * 纳税人类型
     */
    private String taxpayerType;


    /**
     * 开票名称
     */
    private String name;

    /**
     * 识别号
     */
    private String identificationNo;

    /**
     * 开票银行
     */
    private String bank;

    /**
     * 开户银行账号
     */
    private String bankAccountNo;

    /**
     * 注册电话
     */
    private String registerTel;

    /**
     * 注册地址
     */
    private String registerAddr;

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


}
