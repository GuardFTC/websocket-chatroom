package com.ftc.websocketdemo.ws;

import com.alibaba.fastjson2.JSONObject;
import com.ftc.websocketdemo.core.pool.ActiveSessionPool;
import com.ftc.websocketdemo.ws.handler.MessageHandler;
import com.ftc.websocketdemo.ws.handler.MessageHandlerFactory;
import com.ftc.websocketdemo.ws.handler.enums.RoomHandlerTypeEnum;
import com.ftc.websocketdemo.ws.handler.impl.room.LeaveRoomHandler;
import com.ftc.websocketdemo.ws.handler.message.SessionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-05 20:54:08
 * @describe 聊天室Socket处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSocketHandler extends TextWebSocketHandler {

    private final ActiveSessionPool activeSessionPool;

    private final LeaveRoomHandler leaveRoomHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        activeSessionPool.putSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        //1.请求负载参数
        String payload = message.getPayload();
        log.info("服务端收到消息:[{}]", payload);

        //2.解析为session消息体
        SessionMessage sessionMessage = JSONObject.parseObject(payload, SessionMessage.class);

        //3.根据消息类型获取处理器
        MessageHandler handler = MessageHandlerFactory.getHandler(sessionMessage.getHandlerType());

        //4.处理消息
        handler.handleMessage(session, sessionMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        //1.获取会话池移除会话
        activeSessionPool.removeSession(session, status);

        //2.拼接离开房间消息
        JSONObject payload = new JSONObject();
        payload.put("userId", session.getAttributes().get("userId").toString());
        payload.put("roomId", session.getAttributes().get("roomId").toString());
        SessionMessage sessionMessage = new SessionMessage(RoomHandlerTypeEnum.LEAVE_ROOM.getType(), payload.toJSONString());

        //3.处理离开房间逻辑
        leaveRoomHandler.handleMessage(session, sessionMessage);
    }
}

