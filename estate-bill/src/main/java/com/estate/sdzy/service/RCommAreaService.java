package com.estate.sdzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.entity.RCommArea;

import java.util.List;

/**
 * <p>
 * 社区分区表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
public interface RCommAreaService extends IService<RCommArea> {

    List<RCommArea> getCommArea(Long commId);
}
