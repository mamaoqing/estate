package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.RCommArea;
import com.estate.sdzy.mapper.RCommAreaMapper;
import com.estate.sdzy.service.RCommAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
        List<RCommArea> rCommAreas = rCommAreaMapper.selectList(queryWrapper);
        return rCommAreas;
    }
    public List<Map<String,Object>> getAllArea(Long id){
        return commAreaMapper.listCommAreaMap(id);
    }

    @Override
    public List<Map<String, Object>> listAreaMapByUserId(Long userId) {
        return commAreaMapper.listAreaMapByUserId(userId);
    }
}
