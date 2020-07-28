package com.estate.sdzy.service;

import com.estate.sdzy.entity.RCommunity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 社区表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommunityService extends IService<RCommunity> {

    boolean save(RCommunity community,String token);

    boolean saveOrUpdate(RCommunity community,String token);

    boolean removeById(Long id ,String token);

    List<RCommunity> getUserCommunity(List<Long> list);
}
