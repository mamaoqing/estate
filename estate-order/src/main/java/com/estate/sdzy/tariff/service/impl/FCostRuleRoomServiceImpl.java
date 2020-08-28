package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.OrderException;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;
import com.estate.sdzy.tariff.mapper.FCostRuleRoomMapper;
import com.estate.sdzy.tariff.service.FCostRuleRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
@Service
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FCostRuleRoomServiceImpl extends ServiceImpl<FCostRuleRoomMapper, FCostRuleRoom> implements FCostRuleRoomService {

    private final FCostRuleRoomMapper costRuleRoomMapper;
    private final RedisTemplate redisTemplate;

    @Override
    @Transactional
    public boolean insertRoomRule(String token, Map<String,Object> map) {
        Object ruleRooms = map.get("rooms");
        Object ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ruleRooms) && StringUtils.isEmpty(ruleId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        String[] rooms = String.valueOf(ruleRooms).split(",");
        QueryWrapper<FCostRuleRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cost_rule_id",ruleId);
        List<FCostRuleRoom> fCostRuleRooms = costRuleRoomMapper.selectList(queryWrapper);
        // 添加费用标准与物业的关系 ，添加之前需要先将之前存在的该标准的房间删除，然后从新添加
        if (fCostRuleRooms != null && !fCostRuleRooms.isEmpty()) {
            int delete = costRuleRoomMapper.delete(queryWrapper);
            if (!(delete > 0)){
                throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }
        for (String room : rooms) {
            if(StringUtils.isEmpty(room)){
                return true;
            }
            FCostRuleRoom f = new FCostRuleRoom();
            f.setCostRuleId(Long.valueOf(ruleId.toString()));
            f.setPropertyId(Long.valueOf(room));
            f.setPropertyType("room");
            int insert = costRuleRoomMapper.insert(f);
            if(!(insert > 0)){
                throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
            }
        }
        return true;
    }

    @Override
    public List<String> getRoomIds(Long ruleId) {
        String roomIds = costRuleRoomMapper.getRoomIds(ruleId);
        if(StringUtils.isEmpty(roomIds)){
            return  null;
        }
        String[] split = roomIds.split(",");
        return Arrays.asList(split);
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
