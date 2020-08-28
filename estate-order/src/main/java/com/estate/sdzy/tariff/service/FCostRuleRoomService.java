package com.estate.sdzy.tariff.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.tariff.entity.FCostRuleRoom;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-27
 */
public interface FCostRuleRoomService extends IService<FCostRuleRoom> {

    /**
     * 添加费用标准与物业的关系
     * @param token 用户登录凭证
     * @param map 存放ruleId和房间列表
     * @return
     */
    boolean insertRoomRule(String token, Map<String,Object> map);

    List<String> getRoomIds(Long ruleId);
}
