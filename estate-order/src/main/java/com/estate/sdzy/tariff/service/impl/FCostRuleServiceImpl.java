package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.CreateBillDateUtil;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FCostItem;
import com.estate.sdzy.tariff.entity.FCostRule;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;
import com.estate.sdzy.tariff.mapper.FCostItemMapper;
import com.estate.sdzy.tariff.mapper.FCostRuleMapper;
import com.estate.sdzy.tariff.mapper.FCostRuleRoomMapper;
import com.estate.sdzy.tariff.service.FCostRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收费标准 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FCostRuleServiceImpl extends ServiceImpl<FCostRuleMapper, FCostRule> implements FCostRuleService {
    private final FCostRuleMapper costRuleMapper;
    private final RedisTemplate redisTemplate;
    private final FCostItemMapper costItemMapper;
    private final FCostRuleRoomMapper costRuleRoomMapper;

    @Override
    public Page<FCostRule> listCostRule(Map<String, String> map, String token) {
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        QueryWrapper<FCostRule> queryWrapper = new QueryWrapper<>();
        Page<FCostRule> page = new Page<>(pageNo,size);
        SUser user = getUserByToken(token);
        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("aa.comp_id", user.getCompId())
                    // 添加只能查看存在权限的社区条件
                    .eq("aa.is_delete", 0);
//            queryWrapper.in("id",userCommMapper.commIds(user.getId()));
//                    .inSql("aa.id","select  c.comm_id from s_user_comm c where c.user_id= "+user.getId());
        } else {
            // 物业公司
//            queryWrapper.in("comp_id", new ArrayList<>());
            // 删除状态
            queryWrapper.eq(StringUtils.isEmpty(map.get("isDelete")), "aa.is_delete", 0)
                    .eq(!StringUtils.isEmpty(map.get("isDelete")), "aa.is_delete", map.get("isDelete"))
                    .eq(!StringUtils.isEmpty(map.get("compId")),"aa.comp_id", map.get("compId"));
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("costTypeId")),"cost_item_id", map.get("costTypeId"));
        Page<FCostRule> fCostRulePage = costRuleMapper.listCostRule(page, queryWrapper);
        List<FCostRule> records = fCostRulePage.getRecords();
        records.forEach(s->{
        });
        return costRuleMapper.listCostRule(page,queryWrapper);
    }

    @Override
    public List<FCostRule> listAllCostRule(String token) {
        SUser user = getUserByToken(token);
        if(user.getCompId()==0){
            List<FCostRule> listCostRule = costRuleMapper.listAllCostRule(null);
            return listCostRule;
        }else{
            List<FCostRule> listCostRule = costRuleMapper.listAllCostRule(user.getId());
            return listCostRule;
        }
    }

    @Override
    @Transactional
    public boolean save(FCostRule rule, String token) throws ClassNotFoundException {
        SUser user = getUserByToken(token);
        Long costItemId = rule.getCostItemId();
        QueryWrapper<FCostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",costItemId);
        FCostItem fCostItem = costItemMapper.selectOne(queryWrapper);
        rule.setCostTypeId(fCostItem.getCostTypeId());
        rule.setCreatedBy(user.getId());
        rule.setCreatedName(user.getUserName());
        int insert = costRuleMapper.insert(rule);
        if(insert > 0){
            log.info("费用项目添加成功，添加人:{}",user.getUserName());
            Date beginDate = rule.getBeginDate();
            Date endDate = rule.getEndDate();
            Integer billDay = rule.getBillDay();
            Long id = rule.getId();
            String billCycle = rule.getBillCycle();
            CreateBillDateUtil.getBillDate(beginDate,endDate,billDay,id,billCycle);
            return true;
        }
        log.error("费用项目添加失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(FCostRule rule, String token) {
        SUser user = getUserByToken(token);
        rule.setModifiedBy(user.getId());
        rule.setModifiedName(user.getUserName());
        int update = costRuleMapper.updateById(rule);
        if(update > 0){
            log.info("费用项目更新成功，修改人:{}",user.getUserName());
            return true;
        }
        log.error("费用项目修改失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    @Transactional
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        QueryWrapper<FCostRuleRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cost_rule_id",id);
        List<FCostRuleRoom> fCostRuleRooms = costRuleRoomMapper.selectList(queryWrapper);
        if(!fCostRuleRooms.isEmpty()){
            int delete = costRuleRoomMapper.delete(queryWrapper);
            if(!(delete > 0)){
                throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }
        int i = costRuleMapper.deleteById(id);
        if(i>0){
            log.info("费用项目删除成功，删除人:{}",user.getUserName());
            return true;
        }
        log.error("费用项目删除失败");
        throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    public String getCostRuleName(Long id){
        if(costRuleMapper.selectById(id)!=null){
            return costRuleMapper.selectById(id).getName();
        }else{
            return "";
        }
    }

    @Override
    public List<FCostRule> getRuleByOwnerId(Long ownerId) {
        return costRuleMapper.getRuleByOwnerId(ownerId);
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new OrderException(OrderExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
