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
     * 房主ID
     */
    private String ownerUserId;

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
     * @param userId  用户ID
     * @param session 会话
     */
    @SneakyThrows(value = {IOException.class})
    public void addRoomSession(String userId, WebSocketSession session) {

        //1.房间会话Map加入会话
        roomSessions.put(userId, session);

        //2.通知房间所有人
        for (WebSocketSession roomSession : roomSessions.values()) {
            roomSession.sendMessage(new TextMessage("用户" + userId + "已加入房间"));
        }
    }

    /**
     * 移除房间会话
     *
     * @param userId 用户ID
     */
    @SneakyThrows(value = {IOException.class})
    public void removeRoomSession(String userId) {

        //1.从房间会话Map移除会话
        roomSessions.remove(userId);

        //2.通知房间所有人
        for (WebSocketSession roomSession : roomSessions.values()) {
            roomSession.sendMessage(new TextMessage("用户" + userId + "已离开房间"));
        }
    }

    /**
     * 获取房间会话
     *
     * @param userId 用户ID
     * @return WebSocketSession
     */
    public WebSocketSession getRoomSession(String userId) {
        return roomSessions.get(userId);
    }

    /**
     * 变更房主
     *
     * @param session 会话
     */
    @SneakyThrows(value = {IOException.class})
    public void changeOwner(WebSocketSession session) {

        //1.如果已关闭，返回
        if (!session.isOpen()) {
            return;
        }

        //2.如果当前会话不是房主
        if (!session.equals(owner)) {
            return;
        }

        //3.获取房间中的其他会话,设置新房主
        this.owner = roomSessions.values().stream().filter(s -> !s.equals(session)).findFirst().orElse(null);

        //4.如果新房主不为空
        if (ObjectUtil.isNotNull(this.owner)) {

            //5.设置房主ID
            this.ownerUserId = this.owner.getAttributes().get("userId").toString();

            //6.推送消息通知房主变更
            this.owner.sendMessage(new TextMessage("您已成为房主"));
        }
    }
}
