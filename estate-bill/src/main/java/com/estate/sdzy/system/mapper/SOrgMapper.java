package com.estate.sdzy.system.mapper;

import com.estate.sdzy.system.entity.SOrg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SOrgMapper extends BaseMapper<SOrg> {

    List<Map<String,String>> getOnlyChildOrg(@Param("id") Long id);
}
