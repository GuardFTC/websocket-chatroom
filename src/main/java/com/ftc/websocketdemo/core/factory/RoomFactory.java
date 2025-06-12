package com.ftc.websocketdemo.core.factory;

import cn.hutool.core.util.IdUtil;
import com.ftc.websocketdemo.entity.room.Room;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:38:02
 * @describe 房间工厂
 */
public class RoomFactory {

    /**
     * 创建房间
     *
     * @param session 会话
     * @return 房间
     */
    public static Room createRoom(WebSocketSession session, String roomName) {

        //1.创建房间
        Room room = new Room();

        //2.设置房间ID以及房间名称
        String roomId = IdUtil.fastUUID();
        room.setRoomId(roomId);
        room.setRoomName(roomName);

        //3.设置房主
        room.setOwner(session);
        room.setOwnerId(session.getId());

        //4.添加房间会话
        room.addRoomSession(session);

        //5.返回房间
        return room;
    }
}
