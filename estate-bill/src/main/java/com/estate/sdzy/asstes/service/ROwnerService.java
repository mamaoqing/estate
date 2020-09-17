package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
public interface ROwnerService extends IService<ROwner> {
    List<ROwner> getOwenerList(Map map, String token);
    Page<ROwner> getOwenerPageList(Map map, String token);
    boolean insert(ROwner owner, String token);
    boolean update(ROwner owner, String token);
    boolean delete(Long id, String token);
    boolean deleteIds(String delIds, String token);
    List<ROwner> listOwnerByCommId(Map<String,String> map,String token);
    ROwner getCount(ROwner owner, String token);
    List<ROwner> getOwenerByRoom(Map map, String token);
    List<RRoom> selectRoomByOwnerId(Integer ownerId,String token);
    List<RParkingSpace> selectParkByOwnerId(Integer ownerId, String token);
    List<ROwner> getExcel(Map map, String token);
    void saveOrUpdateOwner(ROwner owner, String token);
    Integer selectPageTotal(Map map, String token);
}
