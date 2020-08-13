package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.RCommunity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.RRoom;

import java.util.List;
import java.util.Map;

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

    /**
     * 通过社区的id查询下面的所有的楼栋单元房间
     * @param list
     * @return
     */
    List<Object> getUserCommunity(List<Long> list);

    /**
     * 得到所有的房间
     * @param map
     * @return
     */
    Page<RRoom> getRoomByMap(Map<String,String> map);

    Page<RCommunity> listCommunity(String token,Map<String,String> map);


    List<RCommunity> getUsersComm(String token);
}
