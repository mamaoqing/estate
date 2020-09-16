package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.exception.OrderException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.OrderExceptionEnum;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;
import com.estate.sdzy.tariff.mapper.FCostRuleRoomMapper;
import com.estate.sdzy.tariff.service.FCostRuleRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FCostRuleRoomServiceImpl extends ServiceImpl<FCostRuleRoomMapper, FCostRuleRoom> implements FCostRuleRoomService {

    private final FCostRuleRoomMapper costRuleRoomMapper;
    private final RedisTemplate redisTemplate;

    @Override
    @Transactional
    public boolean insertRoomRule(String token, Map<String, Object> map) {
        Object ruleRooms = map.get("rooms");
        Object ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ruleRooms) && StringUtils.isEmpty(ruleId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        String[] rooms = String.valueOf(ruleRooms).split(",");
        QueryWrapper<FCostRuleRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cost_rule_id", ruleId).eq("property_type", "room");
        List<FCostRuleRoom> fCostRuleRooms = costRuleRoomMapper.selectList(queryWrapper);
        // 添加费用标准与物业的关系 ，添加之前需要先将之前存在的该标准的房间删除，然后从新添加
        if (fCostRuleRooms != null && !fCostRuleRooms.isEmpty()) {
            int delete = costRuleRoomMapper.delete(queryWrapper);
            if (!(delete > 0)) {
                throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
            }
        }
        for (String room : rooms) {
            if (StringUtils.isEmpty(room)) {
                return true;
            }
            FCostRuleRoom f = new FCostRuleRoom();
            f.setCostRuleId(Long.valueOf(ruleId.toString()));
            f.setPropertyId(Long.valueOf(room));
            f.setPropertyType("房产");
            int insert = costRuleRoomMapper.insert(f);
            if (!(insert > 0)) {
                throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean insertParkRule(String token, Map<String, String> map) {
        String ruleParks = map.get("parks");
        String ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ruleParks) && StringUtils.isEmpty(ruleId)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        String[] split = ruleParks.split(",");
        QueryWrapper<FCostRuleRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cost_rule_id", ruleId).eq("property_type", "park");

        for (String room : split) {
            if (StringUtils.isEmpty(room)) {
                return true;
            }
            FCostRuleRoom f = new FCostRuleRoom();
            f.setCostRuleId(Long.valueOf(ruleId));
            f.setPropertyId(Long.valueOf(room));
            f.setPropertyType("停车位");
            int insert = costRuleRoomMapper.insert(f);
            if (!(insert > 0)) {
                throw new OrderException(OrderExceptionEnum.SYSTEM_INSERT_ERROR);
            }
        }
        return true;
    }

    @Override
    public List<String> getRoomIds(Long ruleId) {
        String roomIds = costRuleRoomMapper.getRoomIds(ruleId);
        if (StringUtils.isEmpty(roomIds)) {
            return null;
        }
        String[] split = roomIds.split(",");
        return Arrays.asList(split);
    }

    @Override
    public String getParkIds(Long ruleId) {
        String parkIds = costRuleRoomMapper.getParkIds(ruleId);
        if (StringUtils.isEmpty(parkIds)) {
            return null;
        }
        return parkIds;
    }

    @Override
    public List<Map<String, String>> costPark(Long ruleId) {
        List<Map<String, String>> maps = costRuleRoomMapper.costPark(ruleId);
        return maps;
    }

    @Override
    public boolean removeById(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new OrderException(OrderExceptionEnum.PARAMS_MISS_ERROR);
        }
        int i = costRuleRoomMapper.deleteById(id);
        if (i > 0) {
            log.info("费用标准关系删除成功");
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public boolean removeByIds(Map<String,String> map, String token) {
        String ids = map.get("ids");
        String ruleId = map.get("ruleId");
        if (StringUtils.isEmpty(ids)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        QueryWrapper<FCostRuleRoom> queryWrapper = new QueryWrapper<>();
        String[] split = ids.split(",");
        List<String> strings = Arrays.asList(split);
        queryWrapper.in("property_id", strings).eq("property_type","停车位").eq("cost_rule_id",ruleId);
        int delete = costRuleRoomMapper.delete(queryWrapper);
        if (delete > 0) {
            log.info("删除成功");
            return true;
        }
        throw new OrderException(OrderExceptionEnum.SYSTEM_DELETE_ERROR);
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
