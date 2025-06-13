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
 * @describe 离开房间处理器
 */
@Component
@RequiredArgsConstructor
public class LeaveRoomHandler implements MessageHandler {

    private final RoomPool roomPool;

    private final UserPool userPool;

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.LEAVE_ROOM.getType();
    }

    @Override
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        RoomMessage roomMessage = parsePayload(sessionMessage.getPayload(), RoomMessage.class);

        //2.获取用户ID、房间ID
        String userId = roomMessage.getUserId();
        String roomId = roomMessage.getRoomId();

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

        //5.如果当前会话为房主，则变更房主
        room.changeOwner(session);

        //6.从房间会话移除session
        room.removeRoomSession(userId);

        //7.房间池处理
        roomPool.leaveRoom(userId, room);
    }
}
