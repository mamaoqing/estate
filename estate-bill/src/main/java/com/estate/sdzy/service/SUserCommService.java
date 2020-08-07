package com.estate.sdzy.service;

import com.estate.sdzy.entity.SUserComm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    /**
     * 设置用户数据权限
     * @param userId 用户id
     * @param commIds 用户权限集合
     * @param token 登录凭证
     * @param remark 备注
     * @return 返回一个boolean值
     */
    boolean setUserComm(Long userId,String commIds,String token,String remark);

    /**
     * 通过用户id，查询用户权限
     * @param id 用户id
     * @return 返回用户权限的集合
     */
    List<Map<String,String>> listUserComm(Long compId,Long id);
}
