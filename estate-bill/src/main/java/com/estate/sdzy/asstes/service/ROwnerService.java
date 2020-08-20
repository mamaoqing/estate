package com.estate.sdzy.asstes.service;

import com.estate.sdzy.asstes.entity.ROwner;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
