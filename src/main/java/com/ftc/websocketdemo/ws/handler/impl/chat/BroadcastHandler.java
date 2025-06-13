package com.ftc.websocketdemo.ws.handler.impl.chat;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.core.pool.user.UserPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.entity.user.User;
import com.ftc.websocketdemo.ws.handler.MessageHandler;
import com.ftc.websocketdemo.ws.handler.enums.ChatHandlerTypeEnum;
import com.ftc.websocketdemo.ws.handler.message.ChatMessage;
import com.ftc.websocketdemo.ws.handler.message.SessionMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:45:34
 * @describe 群发消息处理器
 */
@Component
@RequiredArgsConstructor
public class BroadcastHandler implements MessageHandler {

    private final RoomPool roomPool;

    private final UserPool userPool;

    @Override
    public String getHandlerType() {
        return ChatHandlerTypeEnum.BROADCAST.getType();
    }

    @Override
    @SneakyThrows(value = {IOException.class})
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        ChatMessage chatMessage = parsePayload(sessionMessage.getPayload(), ChatMessage.class);

        //2.获取用户ID、房间ID
        String userId = chatMessage.getUserId();
        String roomId = chatMessage.getRoomId();

        //3.通过用户ID获取用户
        User user = userPool.getUser(userId);
        if (ObjectUtil.isNull(user)) {
            return;
        }

        //4.通过房间ID获取房间
        Room room = roomPool.getRoom(roomId);
        if (ObjectUtil.isNull(room)) {
            return;
        }

        //5.获取房间会话
        Map<String, WebSocketSession> roomSessions = room.getRoomSessions();

        //6.广播房间消息
        for (WebSocketSession roomSession : roomSessions.values()) {
            roomSession.sendMessage(new TextMessage(chatMessage.getMessage()));
        }
    }
}
