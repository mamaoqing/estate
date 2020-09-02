package com.estate.sdzy.tariff.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FBillDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 费用标准id
     */
    private Long costRuleId;

    /**
     * 创建账单时间
     */
    private Date createBillDate;

    /**
     * 修改人
     */
    private String updateUser;


}
