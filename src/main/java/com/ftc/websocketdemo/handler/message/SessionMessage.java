package com.ftc.websocketdemo.handler.message;

import lombok.Data;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:28:48
 * @describe 会话消息
 */
@Data
public class SessionMessage {

    /**
     * 处理器类型
     */
    private String handlerType;

    /**
     * 消息内容
     */
    private String payload;
}
