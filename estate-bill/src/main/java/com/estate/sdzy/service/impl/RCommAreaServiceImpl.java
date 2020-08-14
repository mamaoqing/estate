package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.RCommArea;
import com.estate.sdzy.mapper.RCommAreaMapper;
import com.estate.sdzy.service.RCommAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private RCommAreaMapper rCommAreaMapper;
    @Override
    public List<RCommArea> getCommArea(Long commId) {
        QueryWrapper<RCommArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",commId);
        List<RCommArea> rCommAreas = rCommAreaMapper.selectList(queryWrapper);
        return rCommAreas;
    }
}
