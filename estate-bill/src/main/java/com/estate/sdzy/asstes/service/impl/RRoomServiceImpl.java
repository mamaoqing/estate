package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.mapper.RRoomMapper;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房间 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
public class RRoomServiceImpl extends ServiceImpl<RRoomMapper, RRoom> implements RRoomService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RRoomMapper rRoomMapper;

    @Override
    public boolean save(RRoom rRoom, String token) {
        SUser user = getUserByToken(token);
        if (null == rRoom) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rRoom.setCreatedBy(user.getId());
        rRoom.setCreatedName(user.getUserName());
        rRoom.setModifiedBy(user.getId());
        rRoom.setModifiedName(user.getUserName());
        int insert = rRoomMapper.insert(rRoom);
        if (insert > 0) {
            log.info("房间添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean update(RRoom rRoom, String token) {
        SUser user = getUserByToken(token);
        if (null == rRoom) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rRoom.setModifiedBy(user.getId());
        rRoom.setModifiedName(user.getUserName());
        int update = rRoomMapper.updateById(rRoom);
        if (update > 0) {
            log.info("房间修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        RRoom rRoom = rRoomMapper.selectById(id);
        rRoom.setModifiedBy(user.getId());
        rRoom.setModifiedName(user.getUserName());
        rRoom.setIsDelete(1);
        QueryWrapper<RRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        int delete = rRoomMapper.update(rRoom,queryWrapper);
        if(delete>0){
            log.info("房间删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete>0;
    }

    @Override
    public List<RRoom> list(Map<String, String> map, Integer pageNo, Integer size, String token) {

        return null;
    }

    @Override
    public Integer listNum(Map<String, String> map, String token) {
        return null;
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
