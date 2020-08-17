package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.system.entity.SCompLink;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 公司联系人 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SCompLinkService extends IService<SCompLink> {

    /**
     * 根据公司id查询所有的联系人
     * @param id 公司id
     * @param map 条件集合
     * @return
     */
    Page<SCompLink> listCompLink(Long id, Map<String,String> map);

    boolean save(SCompLink sCompLink,String token);

    boolean saveOrUpdate(SCompLink sCompLink,String token);

    boolean removeById(Long id,String token);

}
