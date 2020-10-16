package com.estate.sdzy.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.estate.common.entity.SUser;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.sdzy.system.entity.SMessage;
import com.estate.sdzy.system.entity.SMessageComm;
import com.estate.sdzy.system.mapper.SMessageCommMapper;
import com.estate.sdzy.system.mapper.SMessageMapper;
import com.estate.sdzy.system.service.SMessageService;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.estate.sdzy.wechat.util.SendMessage;
import com.estate.util.UploadFileToFileServer;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Service
@Slf4j
public class SMessageServiceImpl extends ServiceImpl<SMessageMapper, SMessage> implements SMessageService {

    @Autowired
    private SMessageMapper sMessageMapper;
    @Autowired
    private SMessageCommMapper sMessageCommMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<Map<String, String>> listMessageComm(Long compId, Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        return sMessageMapper.listMessageComm(id, compId);
    }

    @Override
    public boolean setMessageComm(Long id, String commIds, String token) {
        String[] split = commIds.split(",");
        SUser user = getUserByToken(token);
        List<Map<String,Object>> longs = sMessageMapper.listOwnerId(split);
        SMessage sMessage = sMessageMapper.selectById(id);
        sMessage.setReleaseAt(new Date());
        sMessage.setReleaseBy(user.getId());
        sMessage.setReleaseName(user.getName());
        sMessage.setState("发布");
        int update = sMessageMapper.updateById(sMessage);
        if(update > 0){
            log.info("消息通知{}修改成功，修改人{}",user.getUserName());
            for (Map<String, Object> map : longs) {
                SMessageComm messageComm = new SMessageComm();
                messageComm.setCommId((Long) map.get("commId"));
                messageComm.setMessageId(sMessage.getId());
                messageComm.setOwnerId((Long) map.get("id"));
                messageComm.setCompId(sMessage.getCompId());
                messageComm.setReleaseBy(user.getId());
                messageComm.setReleaseName(user.getName());
                messageComm.setReleaseAt(new Date());
                int insert = sMessageCommMapper.insert(messageComm);
                if(insert>0){
                    log.info("消息通知中间表{}添加成功，添加人{}",user.getUserName());
                    String uri = "http://www.zywit.com.cn/weChat/top?id=" + sMessage.getId();
                    JSONObject jsonObject = SendMessage.sendMessage((String) map.get("wx_openid"), WeChatResources.TEMPLATE_WYGL_ID,
                            sMessage.getTitle(), sMessage.getSynopsis(), uri, "", map.get("name") + "社区物业提醒您及时查看服务信息！");
                }
            }
        }else{
            throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
        }
        return true;
    }

    @Override
    public Page<SMessage> listMessage(Map<String,String> map, Integer pageNo, Integer size, String token) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (!StringUtils.isEmpty(map.get("size"))) {
            size = Integer.valueOf(map.get("size"));
        }
        SUser user = getUserByToken(token);
        Page<SMessage> page = new Page<>(pageNo, size);
        if(user.getCompId()==0){
            Page<SMessage> listSMessage = sMessageMapper.getListSMessage(page,null);
            return listSMessage;
        }else{
            Page<SMessage> listSMessage = sMessageMapper.getListSMessage(page,user.getCompId());
            return listSMessage;
        }
    }

    @Override
    public boolean save(HttpServletRequest request,String token,MultipartFile[] files) {
        SMessage message = new SMessage();
        message.setTitle(request.getParameter("title"));
        message.setSynopsis(request.getParameter("synopsis"));
        message.setContent(request.getParameter("content"));
        message.setMarkdown(request.getParameter("markdown"));
        message.setState(request.getParameter("state"));
        message.setTopPic(saveFile(files, request,null,null));
        SUser user = getUserByToken(token);
        message.setCreatedBy(user.getId());
        message.setCreatedName(user.getUserName());
        message.setCompId(user.getCompId());
        message.setModifiedName(user.getUserName());
        message.setModifiedBy(user.getId());
        int insert = sMessageMapper.insert(message);
        if(insert > 0){
            log.info("消息通知添加成功，添加人{}",user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @Override
    public boolean saveOrUpdate(HttpServletRequest request,String token,MultipartFile[] files) {
        SMessage message = new SMessage();
        message.setId(Long.valueOf(request.getParameter("id")));
        message.setTitle(request.getParameter("title"));
        message.setSynopsis(request.getParameter("synopsis"));
        message.setContent(request.getParameter("content"));
        message.setMarkdown(request.getParameter("markdown"));
        message.setState(request.getParameter("state"));
        String path = saveFile(files, request, request.getParameter("fileList"),Long.valueOf(request.getParameter("id")));
        message.setTopPic(path);
        SUser user = getUserByToken(token);
        message.setModifiedBy(user.getId());
        message.setModifiedName(user.getUserName());
        int insert = sMessageMapper.updateById(message);
        if(insert > 0){
            log.info("消息通知{}修改成功，修改人{}",user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    private String saveFile(MultipartFile[] files, HttpServletRequest request, String fileList,Long id){
        String topPic = "";
        if(!StringUtils.isEmpty(id)){
            SMessage sMessage = sMessageMapper.selectById(id);
            topPic = sMessage.getTopPic();
        }
        StringBuilder paths = new StringBuilder();
        for (MultipartFile file : files) {
            String path = null;
            UploadFileToFileServer server = new UploadFileToFileServer();
            try {
                path = server.fileUpload(file.getBytes(),file.getOriginalFilename());
            }catch (Exception e){

            }
            paths.append(path+",");
        }
        //获取数据库中topPic的值，拼上新加的地址
        if(!StringUtils.isEmpty(topPic)){
            paths.append(topPic);
        }
        return paths.toString();
    }

    @Override
    @Transactional
    public boolean removeById(Long id, String token) {
        SUser user = getUserByToken(token);
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        int i = sMessageMapper.deleteById(id);
        if(i>0){
            log.info("消息通知{}删除成功，删除人{}",user.getUserName());
            return true;
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @Override
    public boolean delPic(String path, String token) {
        UploadFileToFileServer server = new UploadFileToFileServer();
        String s = "";
        try {
            String[] split = path.split(",");
            s = server.delFile(split[0]);
            if("true".equals(s)){
                SMessage message = sMessageMapper.selectById(Long.valueOf(split[1]));
                String topPic = message.getTopPic();
                String[] split1 = topPic.split(",");
                String topPicNew = "";
                for (String s1 : split1) {
                    if(!s1.equals(split[0])){
                        topPicNew += s1+",";
                    }
                }
                message.setTopPic(topPicNew);
                SUser user = getUserByToken(token);
                message.setModifiedBy(user.getId());
                message.setModifiedName(user.getUserName());
                int insert = sMessageMapper.updateById(message);
                if(insert > 0){
                    log.info("消息通知{}修改成功，修改人{}",user.getUserName());
                    return true;
                }else{
                    throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
                }
            }else{
                return false;
            }

        }catch (Exception e){

        }
        return false;
    }

    private SUser getUserByToken(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (null == o) {
            log.error("登录失效，请重新登录。");
            throw new BillException(BillExceptionEnum.LOGIN_TIME_OUT);
        }
        return (SUser) o;
    }
}
