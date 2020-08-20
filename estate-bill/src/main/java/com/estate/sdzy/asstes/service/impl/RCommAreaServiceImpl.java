package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RCommArea;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.mapper.RBuildingMapper;
import com.estate.sdzy.asstes.mapper.RCommAreaMapper;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.asstes.mapper.RUnitMapper;
import com.estate.sdzy.asstes.service.RCommAreaService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import com.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public List<RCommArea> getCommArea(Long commId) {
        QueryWrapper<RCommArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",commId);
        List<RCommArea> rCommAreas = commAreaMapper.selectList(queryWrapper);
        return rCommAreas;
    }

    @Override
    public List<Map<String,Object>> getAllArea(Long id){
        return commAreaMapper.listCommAreaMap(id);
    }

    @Override
    public List<RCommArea> listAreaByUserId(Map map) {
        return commAreaMapper.listAreaByUserId(map);
    }

    @Override
    public Integer selectPageTotal(Map map) {
        return commAreaMapper.selectPageTotal(map);
    }

    @Override
    public List<Map<String, Object>> listAreaMapByUserId(Long userId) {
        return commAreaMapper.listAreaMapByUserId(userId);
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

    @Override
    public List<RCommArea> getArea(Long commId,String token) {
        SUser user = (SUser) redisUtil.get(token);
        QueryWrapper<RCommArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id",commId);
        queryWrapper.eq("is_delete",0);
        return commAreaMapper.selectList(queryWrapper);
    }


    @Override
    public RCommArea getCommAreaContent(Long id) {
        RCommArea rCommArea = commAreaMapper.selectById(id);
        return rCommArea;
    }

    @Override
    public List<RCommArea> listArea(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<RCommArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id", id);
        return commAreaMapper.selectList(queryWrapper);
    }
}
