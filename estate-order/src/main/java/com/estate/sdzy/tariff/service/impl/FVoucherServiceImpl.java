package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.entity.FVoucher;
import com.estate.sdzy.tariff.mapper.FBillMapper;
import com.estate.sdzy.tariff.mapper.FVoucherMapper;
import com.estate.sdzy.tariff.service.FVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-23
 */
@Service
public class FVoucherServiceImpl extends ServiceImpl<FVoucherMapper, FVoucher> implements FVoucherService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private FVoucherMapper mapper;
    @Autowired
    private FBillMapper billMapper;
    @Autowired
    private ROwnerMapper ownerMapper;

    @Override
    public List<FVoucher> getAll(String token) {
        SUser user = getUserByToken(token);
        if (user.getCompId() == 0) {
            return mapper.selectList(null);
        }
        QueryWrapper<FVoucher> w = new QueryWrapper<>();
        w.eq("comp_id", user.getCompId());
        return mapper.selectList(w);
    }


    @Override
    public List<ROwner> getOwners(String token, Map<String, String> map) {
        getUserByToken(token);
        if (StringUtils.isEmpty(map)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        List<ROwner> owners = new ArrayList<>();
        if (map.get("propertyType").equals("业主")) {
            QueryWrapper<ROwner> w = new QueryWrapper<>();
            w.eq("id", map.get("propertyId"));
            owners.addAll(ownerMapper.selectList(w));
        } else {
            owners.addAll(mapper.getOwners(map));
        }
        return owners;
    }

    @Override
    public Long getNo(String token) {
        getUserByToken(token);
        Long no = mapper.getNo();
        if (StringUtils.isEmpty(no)) {
            return no = Long.parseLong("1");
        }
        return no + 1;
    }

    @Override
    public boolean deleteVoucher(Long id, String token) {
        getUserByToken(token);
        int i = mapper.deleteById(id);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(FVoucher voucher, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(voucher)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        voucher.setCreatedName(user.getUserName());
        voucher.setCreatedBy(user.getId());
        int insert = mapper.insert(voucher);
        if (insert > 0) {
            String[] split = voucher.getBillIds().split(",");
            Map<String,String> map = new HashMap<>();
            for (int i=0;i<split.length;i++){
                map.put("id",voucher.getId().toString());
                map.put("billId",split[i]);
                mapper.insertVoucherProperty(map);
            }
            return true;
        }
        return false;
    }


    public SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
