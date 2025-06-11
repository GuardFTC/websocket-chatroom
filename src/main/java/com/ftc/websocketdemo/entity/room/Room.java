package com.ftc.websocketdemo.entity.room;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:05:24
 * @describe 房间
 */
@Data
public class Room {

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房主
     */
    @JsonIgnore
    private WebSocketSession owner;

    /**
     * 房间会话Map
     */
    @JsonIgnore
    private Map<String, WebSocketSession> roomSessions = new ConcurrentHashMap<>();

    /**
     * 添加房间会话
     *
     * @param session 会话
     */
    @SneakyThrows(value = {IOException.class})
    public void addRoomSession(WebSocketSession session) {

        //1.房间会话Map加入会话
        roomSessions.put(session.getId(), session);

        //2.如果不是房主，那么通知房间所有人
        if (!session.equals(owner)) {
            for (WebSocketSession roomSession : roomSessions.values()) {
                roomSession.sendMessage(new TextMessage("用户" + session.getId() + "已加入房间"));
            }
        }
    }

    /**
     * 移除房间会话
     *
     * @param sessionId 会话ID
     */
    @SneakyThrows(value = {IOException.class})
    public void removeRoomSession(String sessionId) {

        //1.从房间会话Map移除会话
        roomSessions.remove(sessionId);

        //2.通知房间所有人
        for (WebSocketSession roomSession : roomSessions.values()) {
            roomSession.sendMessage(new TextMessage("用户" + sessionId + "已离开房间"));
        }
    }

    /**
     * 获取房间会话
     *
     * @param sessionId 会话ID
     * @return WebSocketSession
     */
    public WebSocketSession getRoomSession(String sessionId) {
        return roomSessions.get(sessionId);
    }

    /**
     * 变更房主
     *
     * @param sessionId 会话ID
     */
    @SneakyThrows(value = {IOException.class})
    public void changeOwner(String sessionId) {

        //1.获取当前会话
        WebSocketSession session = roomSessions.get(sessionId);
        if (ObjectUtil.isNull(session)) {
            return;
        }

        //2.如果已关闭，返回
        if (!session.isOpen()) {
            return;
        }

        //3.如果当前会话不是房主
        if (!session.equals(owner)) {
            return;
        }

        //4.获取房间中的其他会话,设置新房主
        this.owner = roomSessions.values().stream()
                .filter(s -> !s.equals(session))
                .findFirst()
                .orElse(null);

        //5.推送消息通知房主变更
        if (ObjectUtil.isNotNull(this.owner)) {
            this.owner.sendMessage(new TextMessage("您已成为房主"));
        }
    }
}
