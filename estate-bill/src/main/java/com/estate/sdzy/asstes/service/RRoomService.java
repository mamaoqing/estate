package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.RRoom;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RRoomService extends IService<RRoom> {

    boolean save(RRoom rRoom, String token);
    boolean update(RRoom rRoom, String token);
    boolean delete(Long id,String token);
    List<RRoom> list(Map<String,String> map, Integer pageNo, Integer size, String token);
    Integer listNum(Map<String,String> map,String token);
}
