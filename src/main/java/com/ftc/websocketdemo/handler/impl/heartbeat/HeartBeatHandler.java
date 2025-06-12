package com.ftc.websocketdemo.handler.impl.heartbeat;

import com.ftc.websocketdemo.handler.MessageHandler;
import com.ftc.websocketdemo.handler.enums.HeartBeatHandlerTypeEnum;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:45:34
 * @describe 心跳消息处理器
 */
@Component
@RequiredArgsConstructor
public class HeartBeatHandler implements MessageHandler {

    @Override
    public String getHandlerType() {
        return HeartBeatHandlerTypeEnum.PING.getType();
    }

    @Override
    @SneakyThrows(value = {IOException.class})
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {
        session.sendMessage(SessionMessage.message(HeartBeatHandlerTypeEnum.PONG.getType(), null));
    }
}
