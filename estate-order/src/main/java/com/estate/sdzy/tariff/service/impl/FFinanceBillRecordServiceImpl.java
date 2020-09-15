package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.exception.BillException;
import com.estate.common.exception.OrderException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FFinanceBillRecord;
import com.estate.sdzy.tariff.mapper.FFinanceBillRecordMapper;
import com.estate.sdzy.tariff.service.FFinanceBillRecordService;
import lombok.RequiredArgsConstructor;
import org.objenesis.ObjenesisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FFinanceBillRecordServiceImpl extends ServiceImpl<FFinanceBillRecordMapper, FFinanceBillRecord> implements FFinanceBillRecordService {


    private final FFinanceBillRecordMapper financeBillRecordMapper;

    @Override
    public Page<FFinanceBillRecord> getFinanceBillRecords(Map<String, String> map, Integer pageNo, Integer size) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        Page<FFinanceBillRecord> page = new Page<>(pageNo,size);
        return financeBillRecordMapper.getFinanceBillRecord(page,map);
    }

    @Override
    public List<Map<String,Object>> getFFinanceBillRecord(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }

        return  financeBillRecordMapper.findMap(id);
    }
}
