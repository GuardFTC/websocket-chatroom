package com.ftc.websocketdemo.core.factory;

import cn.hutool.core.util.IdUtil;
import com.ftc.websocketdemo.entity.room.Room;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:38:02
 * @describe 房间工厂
 */
public class RoomFactory {

    /**
     * 创建房间
     *
     * @param ownerUserId 房主ID
     * @param roomName    房间名称
     * @return 房间
     */
    public static Room createRoom(String ownerUserId, String roomName) {

        //1.创建房间
        Room room = new Room();

        //2.设置房间ID以及房间名称
        room.setRoomId(IdUtil.simpleUUID());
        room.setRoomName(roomName);

        //3.设置房主
        room.setOwnerUserId(ownerUserId);

        //4.返回房间
        return room;
    }
}
