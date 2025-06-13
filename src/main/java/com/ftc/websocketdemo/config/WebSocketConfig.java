package com.ftc.websocketdemo.config;

import com.ftc.websocketdemo.ws.ChatSocketHandler;
import com.ftc.websocketdemo.core.pool.ActiveSessionPool;
import com.ftc.websocketdemo.ws.handler.impl.room.LeaveRoomHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-05 20:53:36
 * @describe WebSocket配置类
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ActiveSessionPool activeSessionPool;

    private final LeaveRoomHandler leaveRoomHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatSocketHandler(activeSessionPool, leaveRoomHandler), "/chat")
                .setAllowedOrigins("*");
    }
}

