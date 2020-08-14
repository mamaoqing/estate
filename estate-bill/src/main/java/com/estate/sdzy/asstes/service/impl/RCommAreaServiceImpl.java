package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.sdzy.asstes.entity.RCommArea;
import com.estate.sdzy.asstes.mapper.RCommAreaMapper;
import com.estate.sdzy.asstes.service.RCommAreaService;
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

    @Override
    public List<RCommArea> getCommArea(Long commId) {
        QueryWrapper<RCommArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",commId);
        List<RCommArea> rCommAreas = commAreaMapper.selectList(queryWrapper);
        return rCommAreas;
    }
    public List<Map<String,Object>> getAllArea(Long id){
        return commAreaMapper.listCommAreaMap(id);
    }

    @Override
    public List<Map<String, Object>> listAreaMapByUserId(Long userId) {
        return commAreaMapper.listAreaMapByUserId(userId);
    }

    @Override
    public RCommArea getCommAreaContent(Long id) {
        RCommArea rCommArea = commAreaMapper.selectById(id);
        return rCommArea;
    }
}
