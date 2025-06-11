package com.ftc.websocketdemo.handler.impl.room;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.handler.enums.RoomHandlerTypeEnum;
import com.ftc.websocketdemo.handler.message.SessionMessage;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.handler.MessageHandler;
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

    @Override
    public String getHandlerType() {
        return RoomHandlerTypeEnum.LEAVE_ROOM.getType();
    }

    @Override
    public void handleMessage(WebSocketSession session, SessionMessage sessionMessage) {

        //1.通过SessionID获取房间
        Room room = roomPool.getRoomBySession(session.getId());
        if (ObjectUtil.isNull(room)) {
            return;
        }

        //2.如果当前会话为房主，则变更房主
        room.changeOwner(session.getId());

        //3.从房间会话移除session
        room.removeRoomSession(session.getId());

        //4.房间池处理
        roomPool.leaveRoom(session, room);
    }
}
