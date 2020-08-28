package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SCompLink;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SCompLinkMapper;
import com.estate.sdzy.system.service.SCompLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 公司联系人 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SCompLinkServiceImpl extends ServiceImpl<SCompLinkMapper, SCompLink> implements SCompLinkService {

    @Autowired
    private SCompLinkMapper compLinkMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Page<SCompLink> listCompLink(Long id, Map<String,String> map) {
        Integer pageNo;
        Integer size;
        if(StringUtils.isEmpty(map.get("pageNo"))){
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        pageNo = Integer.valueOf(map.get("pageNo"));
        size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        Page<SCompLink> page = new Page<>(pageNo,size);
        QueryWrapper<SCompLink> sCompLinkQueryWrapper = new QueryWrapper<>();
        // 条件查询
        sCompLinkQueryWrapper.eq("comp_id",id);
        return compLinkMapper.selectPage(page, sCompLinkQueryWrapper);
    }

    @Override
    public boolean save(SCompLink sCompLink, String token) {
        SUser user = getUserByToken(token);
        sCompLink.setCreatedBy(user.getId());
        sCompLink.setCreatedName(user.getUserName());
        sCompLink.setModifiedBy(user.getId());
        sCompLink.setModifiedName(user.getUserName());
        int insert = compLinkMapper.insert(sCompLink);
        if(insert > 0){
            log.info("添加公司联系人成功。添加联系人为{}",sCompLink.getName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(SCompLink sCompLink, String token) {
        if(StringUtils.isEmpty(sCompLink.getId())){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        sCompLink.setModifiedBy(user.getId());
        sCompLink.setModifiedName(user.getUserName());

        int insert = compLinkMapper.updateById(sCompLink);
        if(insert > 0){
            log.info("修改公司联系人成功。修改联系人为{}",sCompLink.getName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean removeById(Long id, String token) {
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SUser user = getUserByToken(token);
        int delete = compLinkMapper.deleteById(id);

        if(delete > 0){
            log.info("删除公司联系人成功。删除人：{}",user.getUserName());
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
