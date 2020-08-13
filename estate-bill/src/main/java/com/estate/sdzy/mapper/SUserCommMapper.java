package com.estate.sdzy.mapper;

import com.estate.sdzy.entity.SUserComm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户社区表 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-24
 */
@Repository
public interface SUserCommMapper extends BaseMapper<SUserComm> {

    List<Map<String,String>> listUserComm(@Param("userId") Long userId,@Param("compId") Long compId);

    List<Map<String,String>> listAllUserComm(@Param("compId") Long compId);

    List<SUserComm> listCommUser(@Param("userId")Long userId);

    String commIds(@Param("id") Long userId);
}
