package com.estate.sdzy.wechat.util;

import com.estate.sdzy.wechat.entity.News;
import com.estate.sdzy.wechat.entity.NewsMessage;
import com.estate.sdzy.wechat.entity.TextMessage;
import com.estate.sdzy.wechat.resource.WeChatResources;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author mq
 * @description: TODO
 * @title: MessageUtil
 * @projectName estate-parent
 * @date 2020/9/1518:15
 */
public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_SHORT_VIDEO = "shortvideo";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_EVENT = "event";
    public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_EVENT_CLICK = "CLICK";
    public static final String MESSAGE_EVENT_VIEW = "VIEW";
//https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect


    /**
     * xml转map
     * @param request req
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>(16);
        SAXReader reader = new SAXReader();
        InputStream inputStream = request.getInputStream();
        Document doc = reader.read(inputStream);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();

        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        inputStream.close();

        return map;
    }

    /**
     * 文本消息转对象
     * @param textMessage
     * @return
     */
    public static  String  textMessageToXml(TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());
        return xStream.toXML(textMessage);
    }

    /**
     * 图文消息转对象
     * @param newsMessage
     * @return
     */
    public static  String  newsMessageToXml(NewsMessage newsMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",newsMessage.getClass());
        xStream.alias("item",new News().getClass());
        return xStream.toXML(newsMessage);
    }

    /**
     * 图文消息
     * @param toUser
     * @param formUser
     * @return
     */
    public static String initNews(String toUser,String formUser){
        String message = null;
        List<News> list = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        News news = new News();
        news.setTitle("请选择您的物业信息");
        news.setDescription("小区物业与业主信息关联");
        String encode =null;
        try {
            encode = URLEncoder.encode("http://qjwsg.free.idcfengye.com/weChat/getUser", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String replace = WeChatResources.GET_USER_ACC_URL.replace("APPID", WeChatResources.APPID).replace("REDIRECT_URI", encode);
        System.out.println(replace);
        news.setUrl(replace);
        news.setPicUrl("http://111.229.117.106:8080/fileupload/file/icon/e8410001d12e46d7861407ed748f6f6b.jpg");

        list.add(news);
        newsMessage.setToUserName(formUser);
        newsMessage.setFromUserName(toUser);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MessageUtil.MESSAGE_NEWS);
        newsMessage.setArticles(list);
        newsMessage.setArticleCount(list.size());

        message = newsMessageToXml(newsMessage);
        System.out.println(message);
        return message;
    }

    public static String initText(String toUser,String formUser,String content){
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(formUser);
        textMessage.setFromUserName(toUser);
        textMessage.setMsgType(MessageUtil.MESSAGE_TEXT);
        textMessage.setContent(content);
        textMessage.setCreateTime(new Date().getTime());
        return textMessageToXml(textMessage);
    }

    /**
     * 关注事件
     * @return
     */
    public static String subscribe(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注！：");
        return sb.toString();
    }
}
