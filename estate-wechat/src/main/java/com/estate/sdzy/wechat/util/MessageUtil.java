package com.estate.sdzy.wechat.util;

import com.estate.sdzy.wechat.entity.TextMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mq
 * @description: TODO
 * @title: MessageUtil
 * @projectName estate-parent
 * @date 2020/9/1518:15
 */
public class MessageUtil {

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
}
