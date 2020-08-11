package com.estate.sdzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SDict;
import com.estate.sdzy.entity.SDictItem;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.mapper.SDictItemMapper;
import com.estate.sdzy.mapper.SDictMapper;
import com.estate.sdzy.service.SDictItemService;
import com.estate.util.BillExceptionEnum;
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
 * 字典表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SDictItemServiceImpl extends ServiceImpl<SDictItemMapper, SDictItem> implements SDictItemService {
    @Autowired
    private SDictItemMapper sDictItemMapper;
    @Autowired
    private SDictMapper sDictMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean save(SDictItem sDictItem, String token) {
        SUser user = getUserByToken(token);
        if (null == sDictItem) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sDictItem.setCompId(user.getCompId());
        sDictItem.setState("0");
        sDictItem.setCreatedBy(user.getId());
        sDictItem.setCreatedName(user.getUserName());
        sDictItem.setModifiedBy(user.getId());
        sDictItem.setModifiedName(user.getUserName());
        int insert = sDictItemMapper.insert(sDictItem);
        if (insert > 0) {
            log.info("字典添加成功，添加人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
        }
        return insert > 0;
    }

    @Override
    public boolean update(SDictItem sDictItem, String token) {
        SUser user = getUserByToken(token);
        if (null == sDictItem) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        sDictItem.setModifiedBy(user.getId());
        sDictItem.setModifiedName(user.getUserName());
        int update = sDictItemMapper.updateById(sDictItem);
        if (update > 0) {
            log.info("字典修改成功，修改人={}", user.getUserName());
        } else {
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return update > 0;
    }

    @Override
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = sDictItemMapper.deleteById(id);
        if(delete>0){
            log.info("字典删除成功，删除人={}",user.getUserName());
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
        }
        return delete>0;
    }

    @Override
    public Page<SDictItem> listDictItem(Map<String, String> map, Integer pageNo, Integer size,String token) {
        if(StringUtils.isEmpty(pageNo)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SDictItem> page = new Page<>(pageNo,size);
        QueryWrapper<SDictItem> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        if(!StringUtils.isEmpty(map.get("name"))){
            queryWrapper.eq("name",map.get("name"));

        }
        if(!StringUtils.isEmpty(map.get("dictId"))){
            Page<SDict> pageDict = new Page<>();
            QueryWrapper<SDict> queryDict = new QueryWrapper<>();
            queryDict.like("name",map.get("dictId"));
            Page<SDict> sDictItemPage = sDictMapper.selectPage(pageDict, queryDict);
            List<SDict> records = sDictItemPage.getRecords();
            List<Long> dictId = new ArrayList<>();
            for(SDict dict:records){
                dictId.add(dict.getId());
            }
            queryWrapper.in("dict_id",dictId);
        }
        if(getUserByToken(token).getCompId()!=0){
            List<Long> compId = new ArrayList<>();
            compId.add((long)0);
            compId.add(getUserByToken(token).getCompId());
            queryWrapper.in("comp_id",compId);
        }
        queryWrapper.orderByDesc("dict_id");
        queryWrapper.orderByAsc("order_by");
        Page<SDictItem> sDictItemPage = sDictItemMapper.selectPage(page, queryWrapper);

        return sDictItemPage;
    }

    @Override
    public List<SDictItem> findDictItemList(Map<String, String> map, Integer pageNo, Integer size,String token) {
        String compId="";
        if(getUserByToken(token).getCompId()!=0){
            compId = String.valueOf(getUserByToken(token).getCompId());
        }
        System.out.println((pageNo-1)*size+"----------"+size);
        List<SDictItem> dictItemList = sDictItemMapper.findDictItemList(map.get("name"),map.get("dictId"), (pageNo-1)*size, size,compId);
        for (int i = 0; i <dictItemList.size() ; i++) {
            System.out.print(dictItemList.get(i).getId());
        }
        return dictItemList;
    }

    @Override
    public boolean checkDictItemName(String dictId, String name,String token) {
        Page<SDictItem> page = new Page<>();
        QueryWrapper<SDictItem> queryWrapper = new QueryWrapper<>();
        // 下面放查询条件
        // 名称查询
        if(!StringUtils.isEmpty(dictId)&&!StringUtils.isEmpty(name)){
            queryWrapper.eq("dict_id",dictId);
            queryWrapper.eq("name",name);

            if(getUserByToken(token).getCompId()!=0){
                List<Long> compId = new ArrayList<>();
                compId.add((long)0);
                compId.add(getUserByToken(token).getCompId());
                queryWrapper.in("comp_id",compId);
            }

        }
        //queryWrapper.orderByDesc("id");
        Page<SDictItem> sDictItemPage = sDictItemMapper.selectPage(page, queryWrapper);
        //Boolean a=sDictItemPage.getRecords().size()>0;
        return sDictItemPage.getRecords().size()>0;
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
