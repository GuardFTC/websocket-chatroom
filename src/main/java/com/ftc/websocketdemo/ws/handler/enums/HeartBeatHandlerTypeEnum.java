package com.ftc.websocketdemo.ws.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:19:36
 * @describe 心跳消息处理器类型枚举
 */
@Getter
@AllArgsConstructor
public enum HeartBeatHandlerTypeEnum {

    /**
     * ping
     */
    PING("ping"),

    /**
     * pong
     */
    PONG("pong"),

    /**
     * 获取会话ID
     */
    GET_SESSION_ID("getSessionId"),
    ;

    /**
     * 消息处理器类型
     */
    private final String type;
}
