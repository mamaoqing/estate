package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.tariff.entity.FCostItem;
import com.estate.sdzy.tariff.entity.FCostType;
import com.estate.sdzy.tariff.mapper.FCostItemMapper;
import com.estate.sdzy.tariff.service.FCostItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.sdzy.tariff.service.FCostTypeService;
import com.estate.util.BillExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用项目 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class FCostItemServiceImpl extends ServiceImpl<FCostItemMapper, FCostItem> implements FCostItemService {
    private final FCostItemMapper costItemMapper;
    private final RedisTemplate redisTemplate;
    private final FCostTypeService costTypeService;

    @Override
    public Page<FCostItem> listCostItem(Map<String, String> map,String token) {
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        QueryWrapper<FCostItem> queryWrapper = new QueryWrapper<>();
        Page<FCostItem> page = new Page<>(pageNo,size);

        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("comp_id", user.getCompId())
            // 添加只能查看存在权限的社区条件
            .eq("is_delete", 0)
//            queryWrapper.in("id",userCommMapper.commIds(user.getId()));
            .inSql("id","select  c.comm_id from s_user_comm c where c.user_id= "+user.getId());
        } else {
            // 物业公司
//            queryWrapper.in("comp_id", new ArrayList<>());
            // 删除状态
            queryWrapper.eq(StringUtils.isEmpty(map.get("isDelete")), "aa.is_delete", 0)
            .eq(!StringUtils.isEmpty(map.get("isDelete")), "aa.is_delete", map.get("isDelete"))
            .eq(!StringUtils.isEmpty(map.get("compId")),"comp_id", map.get("compId"));
        }
        queryWrapper.eq(!StringUtils.isEmpty(map.get("no")),"aa.no", map.get("no"))
        .eq(!StringUtils.isEmpty(map.get("name")),"aa.name", map.get("name"))
        .eq(!StringUtils.isEmpty(map.get("costTypeId")),"aa.cost_type_id", map.get("costTypeId"));

        return costItemMapper.listCostItem(page,queryWrapper);
    }

    @Override
    public boolean save(FCostItem item, String token) {
        SUser user = getUserByToken(token);
        item.setCreatedBy(user.getId());
        item.setCreatedName(user.getUserName());
        int insert = costItemMapper.insert(item);
        if (insert > 0) {
            log.info("费用项目添加成功，添加人:{}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(FCostItem item, String token) {
        SUser user = getUserByToken(token);
        item.setModifiedBy(user.getId());
        item.setModifiedName(user.getUserName());
        int update = costItemMapper.updateById(item);
        if(update > 0){
            log.info("费用项目修改成功，修改人:{}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = costItemMapper.deleteById(id);
        if(delete > 0){
            log.info("费用项目删除成功，删除人:{}",user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public List<FCostType> costTypeList() {
        return costTypeService.costTypeList();
    }

    @Override
    public List<FCostItem> costItemList(Long compId) {
        if(StringUtils.isEmpty(compId)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<FCostItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("cost_type_id","id","name").eq("comp_id",compId);
        return costItemMapper.selectList(queryWrapper);
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
