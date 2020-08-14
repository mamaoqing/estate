package com.estate.sdzy.service;

import com.estate.sdzy.entity.RCommArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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
    List<RCommArea> listAreaMapByUserId(Map map);
    List<Map<String,Object>> listAreaMapByUserId(Long userId);
    RCommArea getCommAreaContent(Long id);
    boolean insert(RCommArea commArea,String token);
    boolean delete(Long id,String token);
    boolean update(RCommArea commArea,String token);
}
