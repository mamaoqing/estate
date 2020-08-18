package com.estate.sdzy.asstes.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 停车位 服务类
 * </p>
 *
 * @author mq
 * @since 2020-08-17
 */
public interface RParkingSpaceService extends IService<RParkingSpace> {

    Page<RParkingSpace> listPark(Map<String,String> map, String token);

    boolean save(String token,RParkingSpace parkingSpace);
    boolean saveOrUpdate(String token,RParkingSpace parkingSpace);
    boolean removeById(Long id ,String token);

}
