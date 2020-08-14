package com.estate.sdzy.service;

import com.estate.sdzy.entity.RCommArea;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 社区分区表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommAreaService extends IService<RCommArea> {

    List<RCommArea> getCommArea(Long commId);
    List<Map<String,Object>> getAllArea(Long id);
    List<Map<String,Object>> listAreaMapByUserId(Long userId);
}
