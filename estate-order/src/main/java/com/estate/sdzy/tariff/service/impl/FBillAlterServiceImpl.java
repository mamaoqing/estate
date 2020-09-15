package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.tariff.entity.FBill;
import com.estate.sdzy.tariff.entity.FBillAlter;
import com.estate.sdzy.tariff.mapper.FBillAlterMapper;
import com.estate.sdzy.tariff.mapper.FBillMapper;
import com.estate.sdzy.tariff.service.FBillAlterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@Service
@Slf4j
public class FBillAlterServiceImpl extends ServiceImpl<FBillAlterMapper, FBillAlter> implements FBillAlterService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FBillAlterMapper fBillAlterMapper;

    @Autowired
    private FBillMapper fBillMapper;

    @Override
    public boolean save(FBillAlter fBillAlter, String token) {
        SUser user = getUserByToken(token);
        if (null == fBillAlter) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        fBillAlter.setAlterBy(user.getId());
        fBillAlter.setState("已提交");
        int insert = fBillAlterMapper.insert(fBillAlter);
        if (insert > 0) {
            log.info("费用调整添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    @Transactional
    public String update(FBillAlter fBillAlter, String token) {
        SUser user = getUserByToken(token);
        if (null == fBillAlter) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        FBill fBill = fBillMapper.selectById(fBillAlter.getBillId());
        if("同意".equals(fBillAlter.getState())||"不同意".equals(fBillAlter.getState())){//状态为已提交则证明这是审核修改
            //填写审核意见，点击同意，状态改为同意同时修改账单表里的费用调整金额，需要累积原数据。如果不同意状态改为不同意，不需要修改账单表。
            if("同意".equals(fBillAlter.getState())){
                fBill.setSalePrice(fBill.getSalePrice().add(fBillAlter.getAlterFee()));
                int updateBill = fBillMapper.updateById(fBill);
                if(updateBill>0){
                    int update = fBillAlterMapper.updateById(fBillAlter);
                    if (update > 0) {
                        log.info("费用调整修改成功，修改人={}", user.getUserName());
                    } else {
                        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                    }
                }else{
                    throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                }
                return "1";//费用调整修改成功
            }else{
                int update = fBillAlterMapper.updateById(fBillAlter);
                if (update > 0) {
                    log.info("费用调整修改成功，修改人={}", user.getUserName());
                } else {
                    throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                }
                return "1";//费用调整修改成功
            }
        }else{
            //调整金额的负值不能大于（账单总价格-已经付的钱+逾期产生的费用+费用调整）
            //this.priceCul = list.price-list.payPrice+list.overdueCost+list.salePrice;
            BigDecimal add = fBill.getPrice().subtract(fBill.getPayPrice()).add(fBill.getOverdueCost()).add(fBill.getSalePrice());
            //调整金额的负值大于（账单总价格-已经付的钱+逾期产生的费用+费用调整）
            if(fBillAlter.getAlterFee().multiply(new BigDecimal(-1)).compareTo(add)==1){
                return "2";//提示"减免金额不能大于应付金额"
            }else{
                fBillAlter.setAlterBy(user.getId());
                fBillAlter.setState("已提交");
                fBillAlter.setAudiReason("");
                fBillAlter.setAuditTime(null);
                int update = fBillAlterMapper.updateById(fBillAlter);
                if (update > 0) {
                    log.info("费用调整修改成功，修改人={}", user.getUserName());

                } else {
                    throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
                }
                return "1";//费用调整修改成功
            }

        }
    }

    @Override
    public boolean delete(String id, String token) {
        SUser user = getUserByToken(token);
        int delete = fBillAlterMapper.deleteById(id);
        if (delete > 0) {
            log.info("仪表信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public Page<FBillAlter> list(Map<String, String> map, Integer pageNo, Integer size, String token) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        SUser user = getUserByToken(token);
        Page<SUser> page = new Page<>(pageNo, size);
        if(user.getCompId()==0){
            Page<FBillAlter> listBillAlter = fBillAlterMapper.getListBillAlter(page,map,null,null,null);
            return listBillAlter;
        }else{
            Page<FBillAlter> listBillAlter = fBillAlterMapper.getListBillAlter(page,map,null,null,user.getId());
            return listBillAlter;
        }
    }

    @Override
    public List<FBillAlter> listAll(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        Page<SUser> page = new Page<>();
        if(user.getCompId()==0){
            List<FBillAlter> listBillAlter = fBillAlterMapper.getBillAlterLists(null);
            return listBillAlter;
        }else{
            List<FBillAlter> listBillAlter = fBillAlterMapper.getBillAlterLists(user.getId());
            return listBillAlter;
        }
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
