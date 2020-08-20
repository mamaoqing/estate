package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.exception.BillException;
import com.estate.sdzy.asstes.entity.RCommRoleAgreement;
import com.estate.sdzy.asstes.mapper.RCommRoleAgreementMapper;
import com.estate.sdzy.asstes.service.RCommRoleAgreementService;
import com.estate.sdzy.system.entity.SUser;
import com.estate.util.BillExceptionEnum;
import com.estate.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class RCommRoleAgreementServiceImpl extends ServiceImpl<RCommRoleAgreementMapper, RCommRoleAgreement> implements RCommRoleAgreementService {

    private final RCommRoleAgreementMapper commRoleAgreementMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean isPermission(List<Long> ids) {
        boolean flag = false;
        /**
         * ids 是该用户的所有权限的id集合。
         * 如果ids中的权限没有在r_romm_role_agreement这个表中的话，表示用户没有权限访问该菜单
         * 如果用户有权限访问。则需要判断该权限的时间是否过期，过期返回false 没过期返回true
         */
        if (ids.isEmpty()) {
            return false;
        }
        //
        for (Long id : ids) {
            QueryWrapper<RCommRoleAgreement> agreementQueryWrapper = new QueryWrapper<>();
            agreementQueryWrapper.eq("role_id", id);
            RCommRoleAgreement commRoleAgreement = commRoleAgreementMapper.selectOne(agreementQueryWrapper);
            // 如果查询接口不为空，表示用户有权限，需要进一步验证权限是否过期
            if (null != commRoleAgreement) {

                Date now = new Date();
                Date beginDate = commRoleAgreement.getBeginDate();
                Date endDate = commRoleAgreement.getEndDate();

                if (beginDate.before(now) && endDate.after(now)) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    @Override
    public List<RCommRoleAgreement> getRCommRoleAgreements(String commId) {
        QueryWrapper<RCommRoleAgreement> agreementQueryWrapper = new QueryWrapper<>();
        agreementQueryWrapper.eq("comm_id", commId);
        List<RCommRoleAgreement> rCommRoleAgreements = commRoleAgreementMapper.selectList(agreementQueryWrapper);
        return rCommRoleAgreements;
    }

    @Override
    public Boolean save(RCommRoleAgreement rCommRoleAgreement, String token) {
        SUser user = getUserByToken(token);
        if (null == rCommRoleAgreement) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rCommRoleAgreement.setCreatedBy(user.getId());
        rCommRoleAgreement.setCreatedName(user.getUserName());

        int insert = commRoleAgreementMapper.insert(rCommRoleAgreement);
        if (insert > 0) {
            log.info("协议添加成功。添加人{}，协议开始时间{},协议结束时间{}", user.getUserName(), rCommRoleAgreement.getBeginDate(), rCommRoleAgreement.getEndDate());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public Boolean saveOrUpdate(RCommRoleAgreement rCommRoleAgreement, String token) {
        SUser user = getUserByToken(token);
        if (null == rCommRoleAgreement) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        rCommRoleAgreement.setModifiedBy(user.getId());
        rCommRoleAgreement.setModifiedName(user.getUserName());
        int i = commRoleAgreementMapper.updateById(rCommRoleAgreement);
        if (i > 0) {
            log.info("协议信息更新成功，修改人{}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public Boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int i = commRoleAgreementMapper.deleteById(id);
        if (i > 0) {
            log.info("协议信息删除成功，删除id={},删除人{}", id, user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public Page<RCommRoleAgreement> listCommRoleAgreement(Map<String, String> map, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(map.get("pageNo"))) {
            log.error("参数错误，请输入页码");
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        Integer pageNo = Integer.valueOf(map.get("pageNo"));
        Integer size = StringUtils.isEmpty(map.get("size")) ? 10 : Integer.valueOf(map.get("size"));
        QueryWrapper<RCommRoleAgreement> queryWrapper = new QueryWrapper<>();
        Page<RCommRoleAgreement> page = new Page<>(pageNo, size);

        if (!"超级管理员".equals(user.getType())) {
            queryWrapper.eq("aa.comp_id", user.getCompId());
            // 数据权限，只能查看自己有权限的数据
            queryWrapper.inSql("comm_id", "SELECT c.comm_id FROM s_user_comm c WHERE  c.user_id =  " + user.getId());
        } else {
            queryWrapper.eq(!StringUtils.isEmpty(map.get("compId")), "aa.comp_id", map.get("compId"));
        }
        queryWrapper.eq("aa.is_delete", 0);
        queryWrapper.eq(!StringUtils.isEmpty(map.get("type")), "aa.type", map.get("type"));
        queryWrapper.eq(!StringUtils.isEmpty(map.get("roleId")), "role_id", map.get("roleId"));

        // 当前时间大于结束时间或者小于开始时间，都是已经过期的
        if (!StringUtils.isEmpty(map.get("state")) && map.get("state").equals("over")) {
            queryWrapper.and(queryWrappers -> queryWrappers.lt("end_date", new Date())
                    .or().gt("begin_date", new Date()));
        }
        // 快过期，结束时间
        if (!StringUtils.isEmpty(map.get("state")) && map.get("state").equals("danger")) {
            queryWrapper.lt("end_date", TimeUtil.getBeforeDate(30)).gt("end_date", new Date());
        }
        // 当前时间小于结束时间，大于开始时间，都是有效的。
        queryWrapper.lt(!StringUtils.isEmpty(map.get("state")) && map.get("state").equals("effect"), "begin_date", new Date())
                .gt(!StringUtils.isEmpty(map.get("state")) && map.get("state").equals("effect"), "end_date", new Date());

        return commRoleAgreementMapper.listCommRoleAgreement(page, queryWrapper);
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
