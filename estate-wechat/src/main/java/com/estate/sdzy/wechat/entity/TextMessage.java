package com.estate.sdzy.wechat.entity;

import lombok.Data;

/**
 * @author mq
 * @description: TODO
 * @title: TextMessage
 * @projectName estate-parent
 * @date 2020/9/1518:25
 */
@Data
public class TextMessage extends  BaseMessage{
    private String Content;
    private String MsgId;
}
