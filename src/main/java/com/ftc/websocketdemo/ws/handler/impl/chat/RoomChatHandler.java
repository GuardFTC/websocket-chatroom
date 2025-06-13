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

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:45:34
 * @describe 房间内聊天处理器
 */
@Component
@RequiredArgsConstructor
public class RoomChatHandler implements MessageHandler {

    private final RoomPool roomPool;

    private final UserPool userPool;

    @Override
    public String getHandlerType() {
        return ChatHandlerTypeEnum.ROOM_CHAT.getType();
    }

    @Override
    @SneakyThrows(value = {IOException.class})
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        ChatMessage chatMessage = parsePayload(sessionMessage.getPayload(), ChatMessage.class);

        //2.获取用户ID、房间ID、目标用户ID
        String userId = chatMessage.getUserId();
        String roomId = chatMessage.getRoomId();
        String targetUserId = chatMessage.getTargetUserId();

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

        //5.通过目标用户ID获取用户
        User targetUser = userPool.getUser(targetUserId);
        if (ObjectUtil.isNull(targetUser)) {
            return;
        }

        //6.获取目标会话
        WebSocketSession targetSession = room.getRoomSession(targetUserId);

        //7.如果目标为空或已关闭，返回
        if (ObjectUtil.isNull(targetSession) || !targetSession.isOpen()) {
            return;
        }

        //8.发送消息
        targetSession.sendMessage(new TextMessage(chatMessage.getMessage()));
    }
}
