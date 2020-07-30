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

    /**
     * 通过用户的登录token，查询用户分管的所有的社区id
     * @param token
     * @return 社区id集合
     */
    List<Long> getUserCommIdList(String token);
}
