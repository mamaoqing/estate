package com.estate.sdzy.asstes.mapper;

import com.estate.sdzy.asstes.entity.ROwnerInvoiceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主开票信息 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerInvoiceInfoMapper extends BaseMapper<ROwnerInvoiceInfo> {
    List<ROwnerInvoiceInfo>getAllOwnerInvo(Map map);
    Integer getPageTotal(Map map);
}
