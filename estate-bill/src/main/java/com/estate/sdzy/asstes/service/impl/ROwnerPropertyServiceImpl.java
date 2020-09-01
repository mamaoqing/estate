package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主与物业对应关系 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ROwnerPropertyServiceImpl extends ServiceImpl<ROwnerPropertyMapper, ROwnerProperty> implements ROwnerPropertyService {

    private final  ROwnerPropertyMapper ownerPropertyMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ROwner> ownerProByParkId(Long id) {
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.id",id).eq(" ","停车位").eq("bb.is_delete",0).eq("cc.is_delete",0);

        return ownerPropertyMapper.ownerProByParkId(id,"停车位",0);
    }

    @Override
    public List<ROwnerProperty> getOwnerProperty(Long ownerId) {
        if(StringUtils.isEmpty(ownerId)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return ownerPropertyMapper.getOwnerProperty(ownerId);
    }

    @Override
    public boolean delete(Long id,String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int i = ownerPropertyMapper.deleteById(id);
        if (i>0){
            log.info("业主关系信息删除成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public boolean insertRoomOwner(Map map, String token) {
        SUser user = getUserByToken(token);

        String compId = map.get("compId").toString();
        String commId = map.get("commId").toString();
        String commAreaId = map.get("commAreaId").toString();
        String buildingId = map.get("buildingId").toString();
        String roomId = map.get("roomId").toString();
        String remark = "";
        if (!StringUtils.isEmpty(map.get("remark"))){
            remark = map.get("remark").toString();
        }
        if(StringUtils.isEmpty(map)||StringUtils.isEmpty(compId)||StringUtils.isEmpty(commId)||StringUtils.isEmpty(commAreaId)||StringUtils.isEmpty(buildingId)||StringUtils.isEmpty(roomId)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        String[] ownerIds = map.get("ownerId").toString().split(",");
        String type = map.get("type").toString();

        List<ROwnerProperty> ls = new ArrayList<>();
        for (String ownerId : ownerIds){
            ROwnerProperty property = new ROwnerProperty();
            property.setType(type);
            property.setOwnerId(Long.parseLong(ownerId));
            property.setCompId(Long.parseLong(compId));
            property.setCommId(Long.parseLong(commId));
            property.setCommAreaId(Long.parseLong(commAreaId));
            property.setBuildingId(Long.parseLong(buildingId));
            property.setPropertyType("房产");
            property.setPropertyId(Long.parseLong(roomId));
            QueryWrapper<ROwnerProperty> wrapper = new QueryWrapper<>();
            wrapper.eq("is_delete",0);
            wrapper.eq("comp_id",compId);
            wrapper.eq("comm_id",commId);
            wrapper.eq("comm_area_id",commAreaId);
            wrapper.eq("building_id",buildingId);
            wrapper.eq("property_type","房产");
            wrapper.eq("property_id",roomId);
            wrapper.eq("owner_id",ownerId);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(wrapper);
            if (rOwnerProperties.size()>0){
                return false;
            }
            property.setRemark(remark);
            property.setType(type);
            property.setCreatedBy(user.getId());
            property.setCreatedName(user.getUserName());
            property.setModifiedBy(user.getId());
            property.setModifiedName(user.getUserName());
            ls.add(property);
        }
        boolean b = super.saveBatch(ls);
        if (b){
            log.info("业主关系信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
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
