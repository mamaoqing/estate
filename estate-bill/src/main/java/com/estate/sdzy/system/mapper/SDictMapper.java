package com.estate.sdzy.system.mapper;

import com.estate.sdzy.system.entity.SDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SDictMapper extends BaseMapper<SDict> {

    String getDictName(@Param("name") String name,@Param("id") String id);

    List<String> listDictName(@Param("id") String id);
}
