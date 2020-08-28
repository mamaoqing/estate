package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwnerInvoiceInfo;
import com.estate.sdzy.asstes.mapper.ROwnerInvoiceInfoMapper;
import com.estate.sdzy.asstes.service.ROwnerInvoiceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业主开票信息 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Slf4j
@Service
public class ROwnerInvoiceInfoServiceImpl extends ServiceImpl<ROwnerInvoiceInfoMapper, ROwnerInvoiceInfo> implements ROwnerInvoiceInfoService {

    @Autowired
    private ROwnerInvoiceInfoMapper mapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ROwnerInvoiceInfo> getListByOwnerId(Long ownerId, String token) {
        getUserByToken(token);
        if (null == ownerId) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwnerInvoiceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("owner_id",ownerId);
        return mapper.selectList(wrapper);
    }

    @Override
    public boolean add(ROwnerInvoiceInfo ownerInvoiceInfo, String token) {
        SUser user = getUserByToken(token);
        if (null == ownerInvoiceInfo) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        ownerInvoiceInfo.setCreatedBy(user.getId());
        ownerInvoiceInfo.setCreatedName(user.getUserName());

        int insert = mapper.insert(ownerInvoiceInfo);
        if (insert > 0) {
            log.info("开票信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean update(ROwnerInvoiceInfo ownerInvoiceInfo, String token) {
        SUser user = getUserByToken(token);
        if (null == ownerInvoiceInfo) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        ownerInvoiceInfo.setModifiedBy(user.getId());
        ownerInvoiceInfo.setModifiedName(user.getUserName());

        int update = mapper.updateById(ownerInvoiceInfo);
        if (update > 0) {
            log.info("开票信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }

        int delete = mapper.deleteById(id);
        if (delete > 0) {
            log.info("开票信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public ROwnerInvoiceInfo getInfo(ROwnerInvoiceInfo ownerInvoiceInfo, String token) {
        SUser user = getUserByToken(token);
        if (null == ownerInvoiceInfo) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwnerInvoiceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("taxpayer_type",ownerInvoiceInfo.getTaxpayerType());
        wrapper.eq("identification_no",ownerInvoiceInfo.getIdentificationNo());
        wrapper.orderByDesc("created_at");
        List<ROwnerInvoiceInfo> rOwnerInvoiceInfos = mapper.selectList(wrapper);
        if(rOwnerInvoiceInfos.size()!=0){
            return rOwnerInvoiceInfos.get(0);
        }
        return null;
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
