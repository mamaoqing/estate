package com.estate.sdzy.wechat.resource;

/**
 * @author mq
 * @description: TODO
 * @title: WeChatResources
 * @projectName estate-parent
 * @date 2020/9/1716:08
 */
public class WeChatResources {

    /** appid */
    public static final String APPID = "wx90c1d75d5771fc90";//wx90c1d75d5771fc90

    /** APPSECRET */
    public static final String APPSECRET="bf91b372ce98a6eec65aae6457799308";

    /** 微信支付商户号 */
    public static final String PAYID = "1602968274";

    /** 微信支付密码  819088 */
    public static final String APPPAYSECRET = "7308a4a2c5081dcb34549005cee28b3d";

    /** 微信支付回调地址 */
    public static final String NOTIFYURL = "132.232.89.144/pay/notify";

    /** 微信支付成功时告诉微信 */
    public static final String PAY_SUCCESS_RESULT = "<xml>" +
            "<return_code><![CDATA[SUCCESS]]></return_code>" +
            "<return_msg><![CDATA[OK]]></return_msg>" +
            "</xml>";

    /** 微信支付失败时告诉微信 */
    public static final String PAY_ERROR_RESULT = "<xml>" +
            "<return_code><![CDATA[FAIL]]></return_code>" +
            "<return_msg><![CDATA[ERROR]]></return_msg>" +
            "</xml>";

    /** 微信支付证书保存位置 */
    public static final String CERPATH = "/opt/rh/java/project/apiclient_cert.p12";
//    public static final String CERPATH = "C:\\Users\\Administrator\\Desktop\\WXCertUtil\\cert\\apiclient_cert.p12";

    /** 物业公司id */
    public static final Integer COMP_ID = 1;

    /** 创建菜单 */
    public static final String CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /** 删除菜单 */
    public static final String DELETE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    /** 微信跳转连接 */
    public static  final String GET_USER_ACC_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /** 获取用户授权 */
    public static  final String GET_HTML_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    /** 获取用户信息 openid */
    public static  final String GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /** 获取全局accesstoken */
    public static  final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /** 设置所属行业 post */
    public static final String SET_BUSINESS = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";

    /** 获取设置的行业信息 get */
    public static final String GET_BUSINESS = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";

    /** 获得模板ID post */
    public static final String GET_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";

    /** 获取模板列表 get */
    public static final String GET_TEMPATE_LIST = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN";

    /** 删除模板 post */
    public static final String DELETE_TEMPATE = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN";

    /** 发送模板消息 post */
    public static final String SEND_TEMPATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**  消息模板id */
    public static final String TEMPLATE_WYGL_ID = "8D9nSKZCBVK0Qs7yHM2uEApKLeMxIsP8Zg2vi23JgLo";
}
