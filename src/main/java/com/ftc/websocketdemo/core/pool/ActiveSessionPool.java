package com.ftc.websocketdemo.core.pool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:16:34
 * @describe 活跃会话池
 */
@Slf4j
@Component
public class ActiveSessionPool implements SessionPool {

    /**
     * 活跃会话池
     */
    private static final Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void putSession(WebSocketSession session) {
        SESSION_MAP.put(session.getId(), session);
        log.info("加入活跃会话池:[{}]", session.getId());
    }

    @Override
    public void removeSession(WebSocketSession session, CloseStatus status) {
        SESSION_MAP.remove(session.getId());
        log.info("移除活跃会话池:[{}] 链接关闭状态:[{}]", session.getId(), status.getCode());
    }

    @Override
    public Collection<WebSocketSession> getAllSession() {
        return SESSION_MAP.values();
    }
}
