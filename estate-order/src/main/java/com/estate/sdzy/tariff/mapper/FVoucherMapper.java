package com.estate.sdzy.tariff.mapper;

import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.tariff.entity.FVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-09-23
 */
public interface FVoucherMapper extends BaseMapper<FVoucher> {
    List<ROwner> getOwners (Map<String,String> map);
    Long getNo ();
    boolean insertVoucherProperty(FVoucher voucher);
}
