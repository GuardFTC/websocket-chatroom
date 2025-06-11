package com.ftc.websocketdemo.core.pool.room;

import com.ftc.websocketdemo.entity.room.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
     * 会话-房间映射
     */
    private static final Map<String, Room> SESSION_ROOM_MAP = new ConcurrentHashMap<>();

    /**
     * 添加房间
     *
     * @param room 房间
     */
    public void addRoom(Room room) {

        //1.房间池加入房间
        ROOM_POOL.put(room.getRoomId(), room);

        //2.房间-会话映射加入房主-房间映射
        SESSION_ROOM_MAP.put(room.getOwner().getId(), room);

        //3.日志打印
        log.info("房间[{}]已创建 房主:[{}]", room.getRoomId(), room.getOwner().getId());
    }

    /**
     * 加入房间
     *
     * @param session 会话
     * @param room    房间
     */
    public void joinRoom(WebSocketSession session, Room room) {

        //1.加入会话-房间映射
        SESSION_ROOM_MAP.put(session.getId(), room);

        //2.日志打印
        log.info("[{}]:已加入房间:[{}]", session.getId(), room.getRoomId());
    }

    /**
     * 离开房间
     *
     * @param session 会话
     * @param room    房间
     */
    public void leaveRoom(WebSocketSession session, Room room) {

        //1.从会话-房间映射移除session
        SESSION_ROOM_MAP.remove(session.getId());

        //2.如果房间会话为空，则移除房间
        if (room.getRoomSessions().isEmpty()) {
            ROOM_POOL.remove(room.getRoomId());
            log.info("房间[{}]已关闭", room.getRoomId());
        }
    }

    /**
     * 关闭房间
     *
     * @param room       房间
     * @param sessionIds 房间会话ID列表
     */
    public void closeRoom(Room room, Set<String> sessionIds) {

        //1.从会话-房间映射移除房间会话
        sessionIds.forEach(SESSION_ROOM_MAP::remove);

        //2.从房间池移除房间
        ROOM_POOL.remove(room.getRoomId());

        //3.打印日志
        log.info("房间[{}]已关闭", room.getRoomId());
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
     * @param sessionId 会话ID
     * @return 房间
     */
    public Room getRoomBySession(String sessionId) {
        return SESSION_ROOM_MAP.get(sessionId);
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

