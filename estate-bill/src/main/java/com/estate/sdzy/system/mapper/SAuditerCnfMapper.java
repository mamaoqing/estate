package com.estate.sdzy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.system.entity.SAuditerCnf;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SAuditerCnfMapper extends BaseMapper<SAuditerCnf> {
    List<SAuditerCnf> getListAuditerCnf(@Param("map") Map<String,String> map,@Param("pageNo") Integer pageNo,
                                        @Param("size") Integer size,@Param("userId") Long userId);
    Integer getListAuditerCnfNum(@Param("map") Map<String,String> map,@Param("pageNo") Integer pageNo,
                                        @Param("size") Integer size,@Param("userId") Long userId);
}
