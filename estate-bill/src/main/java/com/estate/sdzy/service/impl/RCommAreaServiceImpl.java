package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.RCommArea;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.RBuildingMapper;
import com.estate.sdzy.mapper.RCommAreaMapper;
import com.estate.sdzy.mapper.RRoomMapper;
import com.estate.sdzy.mapper.RUnitMapper;
import com.estate.sdzy.service.RCommAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.BillExceptionEnum;
import com.estate.util.RedisUtil;
import com.estate.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
@Slf4j
@Service
public class RCommAreaServiceImpl extends ServiceImpl<RCommAreaMapper, RCommArea> implements RCommAreaService {
    @Autowired
    private RCommAreaMapper commAreaMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RBuildingMapper buildingMapper;
    @Autowired
    private RUnitMapper unitMapper;
    @Autowired
    private RRoomMapper rRoomMapper;

    public List<Map<String,Object>> getAllArea(Long id){
        return commAreaMapper.listCommAreaMap(id);
    }

    @Override
    public List<RCommArea> listAreaMapByUserId(Map map) {
        return commAreaMapper.listAreaMapByUserId(map);
    }

    @Override
    public boolean insert(RCommArea commArea,String token) {
        SUser user = (SUser) redisUtil.get(token);
        if (null == commArea) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        commArea.setCreatedBy(user.getId());
        commArea.setCreatedName(user.getUserName());

        int i = commAreaMapper.insert(commArea);
        if(i>0){
            log.info("分区信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean delete(Long id,String token) {
        SUser user = (SUser) redisUtil.get(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }

        int i = commAreaMapper.deleteById(id);
        if(i>0){
            // 删除社区之后，将社区分区，楼栋信息，单元信息，房间信息都逻辑删除
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comm_id",id);
            commAreaMapper.delete(queryWrapper);
            buildingMapper.delete(queryWrapper);
            unitMapper.delete(queryWrapper);
            rRoomMapper.delete(queryWrapper);
            log.info("分区信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean update(RCommArea commArea, String token) {
        SUser user = (SUser) redisUtil.get(token);
        if (null == commArea) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        commArea.setCreatedBy(user.getId());
        commArea.setCreatedName(user.getUserName());
        commArea.setModifiedBy(user.getId());
        commArea.setModifiedName(user.getUserName());
        int update = commAreaMapper.updateById(commArea);
        if (update > 0) {
            log.info("分区信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

}
