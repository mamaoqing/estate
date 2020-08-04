package com.estate.sdzy.service.impl;

import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.entity.SOrg;
import com.estate.sdzy.mapper.SOrgMapper;
import com.estate.sdzy.service.SOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
