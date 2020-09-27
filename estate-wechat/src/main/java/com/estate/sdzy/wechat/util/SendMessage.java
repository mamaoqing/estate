package com.estate.sdzy.wechat.util;

import com.estate.common.util.FormatUtil;
import com.estate.sdzy.wechat.entity.Inform;
import com.estate.sdzy.wechat.resource.WeChatResources;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.util.Date;

/**
 * @author mq
 * @description: TODO
 * @title: SendMessage
 * @projectName estate-parent
 * @date 2020/9/219:27
 */
@Slf4j
public class SendMessage {

    public static void sendMessage(Inform inform, String toUser, String template_id) {
        String token = AccessTokenUtil.getToken();

        JSONObject jsonObject = messageContent(inform, toUser, template_id);

        String url = WeChatResources.SEND_TEMPATE.replace("ACCESS_TOKEN", token);

        JSONObject jsonObject1 = HttpUtil.doPost(url, jsonObject.toString());
        if (Integer.parseInt(jsonObject1.get("errcode").toString()) == 0) {
            String sql = "insert into  ()values()";
            log.info("消息发送成功！");
        } else {
            log.error("消息发送失败，失败编码：{},错误信息：{}", jsonObject1.get("errcode"), jsonObject1.get("errmsg"));
        }
    }

    /**
     * @param toUser      发送给用户的openid
     * @param template_id 模板id
     * @param title       标题
     * @param content     内容
     * @param uri         链接
     * @param remarks     备注
     */
    public static JSONObject sendMessage(String toUser, String template_id, String title, String content, String uri, String remarks,String comm) {
        String token = AccessTokenUtil.getToken();

        JSONObject jsonObject = messageContent(toUser, template_id, title, content, uri, remarks,comm);

        String url = WeChatResources.SEND_TEMPATE.replace("ACCESS_TOKEN", token);

        JSONObject jsonObject1 = HttpUtil.doPost(url, jsonObject.toString());
        if (Integer.parseInt(jsonObject1.get("errcode").toString()) == 0) {
            String sql = "insert into  ()values()";

            log.info("消息发送成功！");
        } else {
            log.error("消息发送失败，失败编码：{},错误信息：{}", jsonObject1.get("errcode"), jsonObject1.get("errmsg"));
        }
        return jsonObject1;
    }


    public static JSONObject sendMessage(String toUser,String template_id, String title, String content,String first ){
        String token = AccessTokenUtil.getToken();
        JSONObject jsonObject = messageContent(toUser, template_id, title, content,first);
        String url = WeChatResources.SEND_TEMPATE.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject1 = HttpUtil.doPost(url, jsonObject.toString());
        log.info("发送内容：{}",jsonObject1);
        return jsonObject1;
    }

    public static void main(String[] args) {
        String token = AccessTokenUtil.getToken();
        System.out.println(token);
//        sendMessage();
    }

    /**
     * @param inform      需要发送的信息内容
     * @param toUser      发送给的用户openid
     * @param template_id 模板id
     * @return 返回一个json字符串
     */
    public static JSONObject messageContent(Inform inform, String toUser, String template_id) {
//        String s = FormatUtil.dateToString(new Date(), FormatUtil.FORMAT_LONG);
        JSONObject jsonObject = new JSONObject();

//        JSONObject data = new JSONObject();
//
//        JSONObject first = new JSONObject();
//        JSONObject keyword1 = new JSONObject();
//        JSONObject keyword2 = new JSONObject();
//        JSONObject pay = new JSONObject();
//        JSONObject remark = new JSONObject();

        jsonObject.put("touser", toUser);
        jsonObject.put("template_id", template_id);
        jsonObject.put("url", inform.getReturnUrl());

//        first.put("value",inform.getTitle());
//        first.put("color",inform.getTopColor());
//        JSONObject josn = inform.getData();
//
//        keyword1.put("value",content);
//        keyword2.put("value",s);
//
//        remark.put("value",remarks);
//
//        pay.put("value","1985.6");
//
//        data.put("first",first);
//        data.put("userName",keyword1);
//        data.put("address",keyword2);
//        data.put("pay",pay);
//        data.put("remark",remark);

        jsonObject.put("data", inform.getData());

        return jsonObject;
    }

    /**
     * @param toUser      发送用户
     * @param template_id 模板消息id
     * @param title       标题
     * @param content     内容
     * @param url         链接
     * @param remarks     备注
     * @return 返回一个信息
     */
    public static JSONObject messageContent(String toUser, String template_id, String title, String content, String url, String remarks,String comm) {
        String s = FormatUtil.dateToString(new Date(), FormatUtil.FORMAT_LONG);
        JSONObject jsonObject = new JSONObject();

        JSONObject data = new JSONObject();

        JSONObject first = new JSONObject();
        JSONObject keyword1 = new JSONObject();
        JSONObject keyword2 = new JSONObject();
        JSONObject keyword3 = new JSONObject();
        JSONObject remark = new JSONObject();

        jsonObject.put("touser", toUser);
        jsonObject.put("template_id", template_id);
        jsonObject.put("url", url);

        first.put("value", comm);
        keyword1.put("value", title);
        keyword2.put("value", s);
        keyword3.put("value",content);
        remark.put("value",remarks);

        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("keyword3", keyword3);
        data.put("remark", remark);

        jsonObject.put("data", data);

        return jsonObject;
    }

    public static JSONObject messageContent(String toUser, String template_id, String title, String content,String firsts) {
        String s = FormatUtil.dateToString(new Date(), FormatUtil.FORMAT_LONG);
        JSONObject jsonObject = new JSONObject();

        JSONObject data = new JSONObject();

        JSONObject first = new JSONObject();
        JSONObject keyword1 = new JSONObject();
        JSONObject keyword2 = new JSONObject();
        JSONObject keyword3 = new JSONObject();
        JSONObject remark = new JSONObject();

        jsonObject.put("touser", toUser);
        jsonObject.put("template_id", template_id);

        first.put("value", firsts);
        keyword1.put("value", title);
        keyword2.put("value", s);
        keyword3.put("value",content);
        remark.put("value","");

        data.put("first", first);
        data.put("keyword1", keyword1);
        data.put("keyword2", keyword2);
        data.put("keyword3", keyword3);
        data.put("remark", remark);

        jsonObject.put("data", data);

        return jsonObject;
    }
}
