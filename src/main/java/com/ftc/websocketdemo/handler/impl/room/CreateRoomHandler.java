package com.ftc.websocketdemo.handler.impl.room;

import com.ftc.websocketdemo.core.factory.RoomFactory;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import com.ftc.websocketdemo.handler.message.RoomMessage;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:45:34
 * @describe 创建房间处理器
 */
@Component
@RequiredArgsConstructor
public class CreateRoomHandler implements MessageHandler {

    private final RoomPool RoomPool;

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.CREATE_ROOM.getType();
    }

    @Override
    @SneakyThrows(value = {IOException.class})
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.解析消息体
        RoomMessage roomMessage = parsePayload(sessionMessage.getPayload(), RoomMessage.class);

        //2.创建房间
        Room room = RoomFactory.createRoom(session, roomMessage.getRoomName());

        //3.房间池处理
        RoomPool.addRoom(room);

        //4.推送客户端消息
        session.sendMessage(new TextMessage(room.getRoomId() + "房间创建成功"));
    }
}
