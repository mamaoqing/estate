package com.estate.sdzy.wechat.entity;

import lombok.Data;

/**
 * @author mq
 * @description: TODO
 * @title: BaseButton
 * @projectName estate-parent
 * @date 2020/9/1715:58
 */
@Data
public class BaseButton {

    private String name ;

    private String type;

    private BaseButton[] sub_button;
}
