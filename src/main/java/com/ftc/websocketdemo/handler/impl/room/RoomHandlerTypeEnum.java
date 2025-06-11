package com.ftc.websocketdemo.handler.impl.room;

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
     * 创建房间
     */
    CREATE_ROOM("createRoom"),

    /**
     * 加入房间
     */
    JOIN_ROOM("joinRoom"),

    /**
     * 离开房间
     */
    LEAVE_ROOM("leaveRoom"),

    /**
     * 关闭房间
     */
    CLOSE_ROOM("closeRoom"),
    ;

    /**
     * 消息处理器类型
     */
    private final String type;
}
