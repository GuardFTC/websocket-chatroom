package com.ftc.websocketdemo.handler.impl.room;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import com.ftc.websocketdemo.handler.message.RoomMessage;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.handler.MessageHandler;
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

    private final RoomPool RoomPool;

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.JOIN_ROOM.getType();
    }

    @Override
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        RoomMessage roomMessage = parsePayload(sessionMessage.getPayload(), RoomMessage.class);

        //2.获取房间ID
        String roomId = roomMessage.getRoomId();

        //3.通过房间ID获取房间
        Room room = RoomPool.getRoom(roomId);
        if (ObjectUtil.isNull(room)) {
            return;
        }

        //4.加入房间会话
        room.addRoomSession(session);

        //5.房间池处理
        RoomPool.joinRoom(session, room);
    }
}
