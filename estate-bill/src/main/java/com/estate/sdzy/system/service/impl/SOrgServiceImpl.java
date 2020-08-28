package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SCompany;
import com.estate.sdzy.system.entity.SOrg;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.mapper.SOrgMapper;
import com.estate.sdzy.system.mapper.SUserMapper;
import com.estate.sdzy.system.service.SOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.util.MenuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SOrgServiceImpl extends ServiceImpl<SOrgMapper, SOrg> implements SOrgService {

    @Autowired
    private SOrgMapper orgMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private SUserMapper userMapper;

    @Override
    public boolean autoSave(SCompany company) {
        Long companyId = company.getId();
        String abbreviation = company.getAbbreviation();
        String remark = "添加公司自动生成机构信息";
        SOrg org = new SOrg(companyId,abbreviation,remark);
        int insert = orgMapper.insert(org);
        if(insert > 0){
            log.info("自动生成公司机构信息成功。");
            return true;
        }
        return false;
    }

    @Override
    public List<SOrg> listOrg(String token) {
        SUser user = getUserByToken(token);
        // 获取公司id，根据公司id查询公司下全部的机构信息
        Long compId = user.getCompId();
        QueryWrapper<SOrg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comp_id",compId).select("id","name","parent_id","parent_id_list","state","remark");
        List<SOrg> orgList = orgMapper.selectList(queryWrapper);
        return MenuUtil.orgList(orgList);
    }

    @Override
    public boolean save(SOrg org, String token) {
        SUser user = getUserByToken(token);
        org.setCreatedBy(user.getId());
        org.setCreatedName(user.getUserName());
        org.setCompId(user.getCompId());
        org.setModifiedName(user.getUserName());
        org.setModifiedBy(user.getId());
        int insert = orgMapper.insert(org);
        if(insert > 0){
            log.info("组织机构{}添加成功，添加人{}",org.getName(),user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(SOrg org, String token) {
        SUser user = getUserByToken(token);
        org.setModifiedBy(user.getId());
        org.setModifiedName(user.getUserName());
        int insert = orgMapper.updateById(org);
        if(insert > 0){
            log.info("组织机构{}修改成功，修改人{}",org.getName(),user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    @Transactional
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        SOrg sOrg = orgMapper.selectById(id);
        String concat = sOrg.getParentIdList().concat(",").concat(id + "");
        int i = orgMapper.deleteById(id);
        if(i>0){
            QueryWrapper<SOrg> orgQueryWrapper = new QueryWrapper<>();
            orgQueryWrapper.and(sOrgQueryWrapper -> sOrgQueryWrapper.like("parent_id_list",concat+",").or().eq("parent_id_list",concat));
            int delete = orgMapper.delete(orgQueryWrapper);
            userMapper.updateUserOrg(id);
            log.info("组织机构{}删除成功，删除人{}",sOrg.getName(),user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public List<SOrg> getBaseOrg(Long compId) {
        if(StringUtils.isEmpty(compId)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<SOrg> queryWrapper = new QueryWrapper<SOrg>();
        queryWrapper.eq("comp_id",compId);
        queryWrapper.isNull("parent_id").select("id","name","parent_id");

        return orgMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, String>> getOnlyChildOrg(Long id) {


        return orgMapper.getOnlyChildOrg(id);
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
