package com.estate.sdzy.asstes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.mapper.ROwnerPropertyMapper;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @Override
    public List<ROwner> ownerProByParkId(Long id) {
        if(StringUtils.isEmpty(id)){
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        QueryWrapper<ROwner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cc.id",id).eq(" ","停车位").eq("bb.is_delete",0).eq("cc.is_delete",0);

        return ownerPropertyMapper.ownerProByParkId(id,"停车位",0);
    }
}
