package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.mapper.ROwnerMapper;
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
import java.util.Arrays;
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

    private final ROwnerPropertyMapper ownerPropertyMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ROwnerMapper ownerMapper;

    @Override
    public List<ROwner> ownerProByParkId(Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.id", id).eq(" ", "停车位").eq("bb.is_delete", 0).eq("cc.is_delete", 0);

        return ownerPropertyMapper.ownerProByParkId(id, "停车位", 0);
    }

    @Override
    public List<ROwnerProperty> getOwnerProperty(Long ownerId) {
        if (StringUtils.isEmpty(ownerId)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return ownerPropertyMapper.getOwnerProperty(ownerId);
    }

    @Override
    public List<ROwnerProperty> getAllProperty(Map map, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(map)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (user.getCompId() != 0) {
            map.put("compId", user.getCompId());
        }
        return ownerPropertyMapper.getAllProperty(map);
    }

    @Override
    public boolean update(ROwnerProperty ownerProperty, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(ownerProperty)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        ownerProperty.setModifiedBy(user.getId());
        ownerProperty.setModifiedName(user.getUserName());
        int i = ownerPropertyMapper.updateById(ownerProperty);
        if (i > 0) {
            log.info("业主信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }


    @Override
    public Integer getPageTotal(Map map, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(map)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (user.getCompId() != 0) {
            map.put("compId", user.getCompId());
        }
        return ownerPropertyMapper.getPageTotal(map);
    }

    @Override
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        ROwnerProperty ownerProperty = new ROwnerProperty();
        ownerProperty.setId(id);
        ownerProperty.setIsDelete(1);
        ownerProperty.setCreatedBy(user.getId());
        ownerProperty.setCreatedName(user.getUserName());
        ownerProperty.setModifiedBy(user.getId());
        ownerProperty.setModifiedName(user.getUserName());
        int update = ownerPropertyMapper.updateById(ownerProperty);
        if (update > 0) {
            log.info("业主信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public boolean insertRoomOwnerOrPark(Map map, String token) {
        SUser user = getUserByToken(token);

        String compId = map.get("compId").toString();
        String commId = map.get("commId").toString();
        String commAreaId = map.get("commAreaId").toString();
        String buildingId = null;
        String type = null;
        if (!StringUtils.isEmpty(map.get("buildingId"))) {
            buildingId = map.get("buildingId").toString();
            type = map.get("type").toString();
        }
        String propertyId = map.get("propertyId").toString();
        String propertyType = map.get("propertyType").toString();
        String remark = "";
        if (!StringUtils.isEmpty(map.get("remark"))) {
            remark = map.get("remark").toString();
        }
        if (StringUtils.isEmpty(map) || StringUtils.isEmpty(compId) || StringUtils.isEmpty(commId) || StringUtils.isEmpty(commAreaId) || StringUtils.isEmpty(propertyId)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        String[] ownerIds = map.get("ownerId").toString().split(",");


        List<ROwnerProperty> ls = new ArrayList<>();
        for (String ownerId : ownerIds) {
            ROwnerProperty property = new ROwnerProperty();
            property.setType(type);
            property.setOwnerId(Long.parseLong(ownerId));
            property.setCompId(Long.parseLong(compId));
            property.setCommId(Long.parseLong(commId));
            property.setCommAreaId(Long.parseLong(commAreaId));

            property.setPropertyType(propertyType);
            property.setPropertyId(Long.parseLong(propertyId));
            QueryWrapper<ROwnerProperty> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(map.get("buildingId"))) {
                property.setBuildingId(Long.parseLong(buildingId));
                wrapper.eq("building_id", buildingId);
            }
            wrapper.eq("is_delete", 0);
            wrapper.eq("comp_id", compId);
            wrapper.eq("comm_id", commId);
            wrapper.eq("comm_area_id", commAreaId);

            wrapper.eq("property_type", propertyType);
            wrapper.eq("property_id", propertyId);
            wrapper.eq("owner_id", ownerId);
            List<ROwnerProperty> rOwnerProperties = ownerPropertyMapper.selectList(wrapper);
            if (rOwnerProperties.size() > 0) {
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
        if (b) {
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

    @Override
    public boolean deleteIds(String delIds, String token) {
        SUser user = getUserByToken(token);
        if (null == delIds) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        String[] split = delIds.split(",");
        List<String> ids = Arrays.asList(split);

        int b = ownerPropertyMapper.deleteBatchIds(ids);
        if (b > 0) {
            log.info("业主信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public void saveOrUpdateOwnerProp(ROwnerProperty ownerProperty, String token) {
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comp_id", ownerProperty.getCompId());
        queryWrapper.eq("owner_type", ownerProperty.getOwnerType());
        queryWrapper.eq("cert_type", ownerProperty.getCertType());
        queryWrapper.eq("cert_number", ownerProperty.getCertNumber());
        queryWrapper.eq("is_delete", 0);
        ROwner rOwner = ownerMapper.selectOne(queryWrapper);
        if (rOwner!=null){
            ownerProperty.setOwnerId(rOwner.getId());
        }else{
            throw new BillException(415, "导入失败，业主" + ownerProperty.getOwnerName() + "未找到");
        }

        //保存前进行判断是否已经存在数据
        QueryWrapper<ROwnerProperty> wrapper = new QueryWrapper<>();
        wrapper.eq("comp_id", ownerProperty.getCompId());
        wrapper.eq("owner_id", ownerProperty.getOwnerId());
        wrapper.eq("property_type", ownerProperty.getPropertyType());
        wrapper.eq("property_id", ownerProperty.getPropertyId());
        wrapper.eq("is_delete", 0);
        List<ROwnerProperty> owners = ownerPropertyMapper.selectList(wrapper);
        if (owners.size() == 1) {//执行update
            ownerProperty.setId(owners.get(0).getId());
            update(ownerProperty, token);
        } else if (owners.size() == 0) {//执行新增
            ownerPropertyMapper.insert(ownerProperty);
        } else {
            throw new BillException(415, "导入失败，业主" + ownerProperty.getOwnerName() + "错误");
        }
    }
}
