package com.estate.sdzy.tariff.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.tariff.entity.SBillNotes;
import com.estate.sdzy.tariff.mapper.SBillNotesMapper;
import com.estate.sdzy.tariff.service.SBillNotesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-09-21
 */
@Service
@Slf4j
public class SBillNotesServiceImpl extends ServiceImpl<SBillNotesMapper, SBillNotes> implements SBillNotesService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SBillNotesMapper mapper;

    public List<SBillNotes> getAll(String token) {
        SUser user = getUserByToken(token);

        if (user.getCompId() == 0) {
            return mapper.selectList(null);
        } else {
            QueryWrapper<SBillNotes> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("comp_id", user.getCompId());
            return mapper.selectList(queryWrapper);
        }

    }

    @Override
    public List<SBillNotes> getByCommId(Long commId, String token) {
        SUser user = getUserByToken(token);
        if (null == commId) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<SBillNotes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comm_id", commId);
        return mapper.selectList(queryWrapper);
    }

    public boolean insert(SBillNotes notes, String token) {
        SUser user = getUserByToken(token);
        if (null == notes) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        notes.setCreatedBy(user.getId());
        notes.setCreatedName(user.getUserName());

        int insert = mapper.insert(notes);
        if (insert > 0) {
            log.info("单据备注信息添加成功，添加人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean update(SBillNotes notes, String token) {
        SUser user = getUserByToken(token);
        if (null == notes) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int update = mapper.updateById(notes);
        if (update > 0) {
            log.info("单据备注信息修改成功，修改人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @Override
    public boolean delete(Long id, String token) {
        SUser user = getUserByToken(token);
        if (null == id) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int delete = mapper.deleteById(id);
        if (delete > 0) {
            log.info("单据备注信息删除成功，删除人={}", user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    public SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
