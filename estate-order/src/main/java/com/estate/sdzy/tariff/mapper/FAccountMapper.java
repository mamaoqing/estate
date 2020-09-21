package com.estate.sdzy.tariff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FAccount;
import com.estate.sdzy.tariff.entity.FAccountCostItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户 Mapper 接口
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
public interface FAccountMapper extends BaseMapper<FAccount> {

    Page<FAccount> listAccount(Page<FAccount> page, @Param("map") Map<String,String> map, @Param("userId") Long userId);

    FAccount getAccount(@Param("ownerId") Long ownerId,@Param("ruleId") Long ruleId);

    Long getMaxNo();

    List<Map<String,String>> getRoom(@Param("map") Map<String,String> map);
    List<Map<String,String>> getParking(@Param("map") Map<String,String> map);

    List<FAccountCostItem> getAccountItemByAccountId(@Param("accountId") String accountId);
}
