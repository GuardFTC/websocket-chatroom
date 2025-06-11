package com.ftc.websocketdemo.core.pool;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:15:43
 * @describe 会话池接口
 */
public interface SessionPool {

    /**
     * 添加会话
     *
     * @param session 会话
     */
    void putSession(WebSocketSession session);

    /**
     * 移除会话
     *
     * @param session 会话
     * @param status  关闭状态 {@link CloseStatus}
     */
    void removeSession(WebSocketSession session, CloseStatus status);

    /**
     * 获取所有会话
     *
     * @return 会话列表
     */
    Collection<WebSocketSession> getAllSession();
}
