package com.ftc.websocketdemo.ws.handler.impl.room;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.core.pool.user.UserPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.entity.user.User;
import com.ftc.websocketdemo.ws.handler.MessageHandler;
import com.ftc.websocketdemo.ws.handler.enums.RoomHandlerTypeEnum;
import com.ftc.websocketdemo.ws.handler.message.RoomMessage;
import com.ftc.websocketdemo.ws.handler.message.SessionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:45:34
 * @describe 加入房间处理器
 */
@Component
@RequiredArgsConstructor
public class JoinRoomHandler implements MessageHandler {

    private final RoomPool roomPool;

    private final UserPool userPool;

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.JOIN_ROOM.getType();
    }

    @Override
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        RoomMessage roomMessage = parsePayload(sessionMessage.getPayload(), RoomMessage.class);

        //2.获取用户ID、房间ID
        String userId = roomMessage.getUserId();
        String roomId = roomMessage.getRoomId();

        //3.会话属性添加用户ID、房间ID
        session.getAttributes().put("userId", userId);
        session.getAttributes().put("roomId", roomId);

        //4.通过用户ID获取用户
        User user = userPool.getUser(userId);
        if (ObjectUtil.isNull(user)) {
            return;
        }

        //5.通过房间ID获取房间
        Room room = roomPool.getRoom(roomId);
        if (ObjectUtil.isNull(room)) {
            return;
        }

        //6.如果当前用户为房管，那么设置房管session
        if (room.getOwnerUserId().equals(userId)) {
            room.setOwner(session);
        }

        //7.加入房间会话
        room.addRoomSession(userId, session);

        //8.房间池处理
        roomPool.joinRoom(userId, room);
    }
}
