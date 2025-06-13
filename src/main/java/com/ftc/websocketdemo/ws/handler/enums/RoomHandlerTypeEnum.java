package com.ftc.websocketdemo.ws.handler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:19:36
 * @describe 房间处理器类型枚举
 */
@Getter
@AllArgsConstructor
public enum RoomHandlerTypeEnum {

    /**
     * 加入房间
     */
    JOIN_ROOM("joinRoom"),

    /**
     * 离开房间
     */
    LEAVE_ROOM("leaveRoom"),
    ;

    /**
     * 消息处理器类型
     */
    private final String type;
}
