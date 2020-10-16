package com.estate.sdzy.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.estate.sdzy.system.entity.SMessage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构表 服务类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
public interface SMessageService extends IService<SMessage> {

    Page<SMessage> listMessage(Map<String,String> map, Integer pageNo, Integer size, String token);
    boolean save(HttpServletRequest request, String token,MultipartFile[] files);
    boolean saveOrUpdate(HttpServletRequest request, String token,MultipartFile[] files);
    boolean removeById(Long id,String token);
    boolean delPic(String path,String token);
    List<Map<String,String>> listMessageComm(Long compId, Long id);
    boolean setMessageComm(Long id, String commIds, String token);
}
