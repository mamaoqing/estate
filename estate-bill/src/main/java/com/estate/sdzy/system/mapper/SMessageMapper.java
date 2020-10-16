package com.estate.sdzy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.system.entity.SMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Repository
public interface SMessageMapper extends BaseMapper<SMessage> {

    Page<SMessage>  getListSMessage(Page<SMessage> page,@Param("compId") Long compId);
    List<Map<String,String>> listMessageComm(@Param("id") Long id, @Param("compId") Long compId);
    List<Map<String,Object>> listOwnerId(String[] str);

}
