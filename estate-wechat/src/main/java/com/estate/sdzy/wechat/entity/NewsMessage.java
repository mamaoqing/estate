package com.estate.sdzy.wechat.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mq
 * @description: TODO
 * @title: NewsMessage
 * @projectName estate-parent
 * @date 2020/9/1717:48
 */
@Data
public class NewsMessage extends BaseMessage{
    private int ArticleCount;

    private List<News> Articles;
}
