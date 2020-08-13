package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.RCommArea;
import com.estate.sdzy.mapper.RCommAreaMapper;
import com.estate.sdzy.service.RCommAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区分区表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
public class RCommAreaServiceImpl extends ServiceImpl<RCommAreaMapper, RCommArea> implements RCommAreaService {
    @Autowired
    private RCommAreaMapper commAreaMapper;

    public List<Map<String,Object>> getAllArea(Long id){
        return commAreaMapper.listCommAreaMap(id);
    }

    @Override
    public List<Map<String, Object>> listAreaMapByUserId(Long userId) {
        return commAreaMapper.listAreaMapByUserId(userId);
    }
}
