package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.tariff.entity.FCostRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.sql.SQLException;
import java.util.Map;

/**
 * <p>
 * 收费标准 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface FCostRuleService extends IService<FCostRule> {

    Page<FCostRule> listCostRule(Map<String,String>map,String token);

    boolean save(FCostRule rule,String token) throws SQLException, ClassNotFoundException;
    boolean saveOrUpdate(FCostRule rule,String token);
    boolean removeById(Long id ,String token);
}
