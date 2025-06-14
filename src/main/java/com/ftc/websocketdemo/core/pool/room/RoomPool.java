package com.ftc.websocketdemo.core.pool.room;

import com.ftc.websocketdemo.entity.room.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:33:23
 * @describe 房间池
 */
@Slf4j
@Component
public class RoomPool {

    /**
     * 房间池
     */
    private static final Map<String, Room> ROOM_POOL = new ConcurrentHashMap<>();

    /**
     * 用户ID-房间映射
     */
    private static final Map<String, Room> USERID_ROOM_MAP = new ConcurrentHashMap<>();

    /**
     * 添加房间
     *
     * @param room 房间
     */
    public void addRoom(Room room) {

        //1.房间池加入房间
        ROOM_POOL.put(room.getRoomId(), room);

        //2.用户ID-房间映射加入房主-房间映射
        USERID_ROOM_MAP.put(room.getOwnerUserId(), room);

        //3.日志打印
        log.info("房间[{}]已创建 房主:[{}]", room.getRoomId(), room.getOwnerUserId());
    }

    /**
     * 加入房间
     *
     * @param userId 用户ID
     * @param room   房间
     */
    public void joinRoom(String userId, Room room) {

        //1.加入用户ID-房间映射
        USERID_ROOM_MAP.put(userId, room);

        //2.日志打印
        log.info("[{}]:已加入房间:[{}]", userId, room.getRoomId());
    }

    /**
     * 离开房间
     *
     * @param userId 用户ID
     * @param room   房间
     */
    public void leaveRoom(String userId, Room room) {

        //1.从用户ID-房间映射移除session
        USERID_ROOM_MAP.remove(userId);

        //2.如果房间会话为空，则移除房间
        if (room.getRoomSessions().isEmpty()) {
            ROOM_POOL.remove(room.getRoomId());
            log.info("房间[{}]已关闭", room.getRoomId());
        }
    }

    /**
     * 获取房间
     *
     * @param roomId 房间ID
     * @return 房间
     */
    public Room getRoom(String roomId) {
        return ROOM_POOL.get(roomId);
    }

    /**
     * 获取房间
     *
     * @param userId 用户ID
     * @return 房间
     */
    public Room getRoomByUserId(String userId) {
        return USERID_ROOM_MAP.get(userId);
    }

    /**
     * 获取所有房间
     *
     * @return 房间列表
     */
    public List<Room> getAllRoom() {
        return ROOM_POOL.values().stream().toList();
    }
}

