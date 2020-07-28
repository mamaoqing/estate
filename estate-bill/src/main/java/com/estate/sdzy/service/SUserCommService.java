package com.estate.sdzy.service;

import com.estate.sdzy.entity.SUserComm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户社区表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-24
 */
public interface SUserCommService extends IService<SUserComm> {

    List<Long> getUserCommIdList(String token);
}
