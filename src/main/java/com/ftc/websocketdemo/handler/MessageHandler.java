package com.ftc.websocketdemo.handler;

import com.alibaba.fastjson2.JSONObject;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:14:25
 * @describe 消息处理接口
 */
public interface MessageHandler {

    /**
     * 获取处理类型
     *
     * @return 处理类型
     */
    String getHandlerType();

    /**
     * 处理消息
     *
     * @param session        当前会话
     * @param sessionMessage 会话消息
     */
    void handleMessage(WebSocketSession session, SessionMessage sessionMessage);

    /**
     * 解析消息体
     *
     * @param payload 消息体
     * @return 解析结果
     */
    default <T> T parsePayload(String payload, Class<T> tClass) {
        return JSONObject.parseObject(payload, tClass);
    }
}
