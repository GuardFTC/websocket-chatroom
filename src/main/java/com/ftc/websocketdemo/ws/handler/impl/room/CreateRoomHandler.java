package com.ftc.websocketdemo.ws.handler.impl.room;

import com.alibaba.fastjson2.JSONObject;
import com.ftc.websocketdemo.core.factory.RoomFactory;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.ws.handler.MessageHandler;
import com.ftc.websocketdemo.ws.handler.enums.RoomHandlerTypeEnum;
import com.ftc.websocketdemo.ws.handler.message.RoomMessage;
import com.ftc.websocketdemo.ws.handler.message.SessionMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
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

        //1.通过会话ID获取房间
        Room room = RoomPool.getRoomByUserId(session.getId());

        //2.如果已经是房主，那么直接返回
        if (room != null && room.getOwner().equals(session)) {
            return;
        }

        //3.解析消息体
        RoomMessage roomMessage = parsePayload(sessionMessage.getPayload(), RoomMessage.class);

        //4.创建房间
        room = RoomFactory.createRoom(session, roomMessage.getRoomName());

        //5.房间池处理
        RoomPool.addRoom(room);

        //6.推送客户端消息
        session.sendMessage(SessionMessage.message(RoomHandlerTypeEnum.CREATE_ROOM.getType(), JSONObject.toJSONString(room)));
    }
}
