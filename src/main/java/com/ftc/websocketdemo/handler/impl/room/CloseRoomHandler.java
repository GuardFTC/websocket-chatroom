package com.ftc.websocketdemo.handler.impl.room;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.handler.MessageHandler;
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
 * @describe 关闭房间处理器
 */
@Component
@RequiredArgsConstructor
public class CloseRoomHandler implements MessageHandler {

    private final RoomPool roomPool;

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.CLOSE_ROOM.getType();
    }

    @Override
    @SneakyThrows(value = {IOException.class})
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.通过SessionID获取房间
        Room room = roomPool.getRoomBySession(session.getId());
        if (ObjectUtil.isNull(room)) {
            return;
        }

        //2.如果用户不是房主，直接返回
        if (!room.getOwner().equals(session)) {
            return;
        }

        //3.获取房间会话
        Map<String, WebSocketSession> roomSessions = room.getRoomSessions();

        //4.房主设置为空
        room.setOwner(null);

        //5.设置房间会话为空
        room.setRoomSessions(null);

        //6.房间池操作
        roomPool.closeRoom(room, roomSessions.keySet());

        //7.广播房间已关闭
        for (WebSocketSession roomSession : roomSessions.values()) {
            roomSession.sendMessage(new TextMessage("房间已关闭"));
        }
    }
}
