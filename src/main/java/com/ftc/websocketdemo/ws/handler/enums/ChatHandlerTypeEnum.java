package com.ftc.websocketdemo.ws.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:19:36
 * @describe 聊天消息处理器类型枚举
 */
@Getter
@AllArgsConstructor
public enum ChatHandlerTypeEnum {

    /**
     * 群发消息
     */
    BROADCAST("broadcast"),

    /**
     * 房间内聊天
     */
    ROOM_CHAT("roomChat"),
    ;

    /**
     * 消息处理器类型
     */
    private final String type;
}
