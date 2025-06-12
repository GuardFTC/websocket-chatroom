package com.ftc.websocketdemo.handler.message;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:28:48
 * @describe 会话消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionMessage {

    /**
     * 处理器类型
     */
    private String handlerType;

    /**
     * 消息内容
     */
    private String payload;

    /**
     * 创建文本会话消息
     *
     * @param handlerType 处理器类型
     * @param payload     消息内容
     * @return 会话消息
     */
    public static TextMessage message(String handlerType, String payload) {

        //1.创建会话消息体
        final SessionMessage sessionMessage = new SessionMessage(handlerType, payload);

        //2.创建文本消息返回
        return new TextMessage(JSONObject.toJSONString(sessionMessage));
    }
}
