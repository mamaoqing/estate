package com.estate.sdzy.asstes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.estate.sdzy.asstes.entity.RBuilding;
import org.apache.ibatis.annotations.Param;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 楼房 Mapper 接口
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RBuildingMapper extends BaseMapper<RBuilding> {

    List<Map<String,Object>> listBuildMap(@Param("id") Long areaId);

    @Sql("update r_building set is_delete='1',modified_by=#{id},modified_name=#{name} where id=#{id}")
    int update(@Param("id") Long id,@Param("name") String name);

    List<RBuilding> getListBuilding(@Param("name") String name,@Param("no") String no,@Param("type") String type, @Param("compId") String compId,@Param("commId") String  commId,@Param("commAreaId") String commAreaId ,@Param("pageNo") Integer pageNo,@Param("size") Integer size);
}
