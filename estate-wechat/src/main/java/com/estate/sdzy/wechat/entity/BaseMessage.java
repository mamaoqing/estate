package com.estate.sdzy.wechat.entity;

import lombok.Data;

/**
 * @author mq
 * @description: TODO
 * @title: BaseMessage
 * @projectName estate-parent
 * @date 2020/9/1715:58
 */
@Data
public class BaseMessage {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
}
