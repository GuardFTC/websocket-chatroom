package com.ftc.websocketdemo.ws.handler.message;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:52:24
 * @describe 聊天消息
 */
@Data
public class ChatMessage {

    /**
     * 房间ID
     */
    private String roomId = StrUtil.EMPTY;

    /**
     * 目标会话ID
     */
    private String targetSessionId = StrUtil.EMPTY;

    /**
     * 消息内容
     */
    private String message = StrUtil.EMPTY;
}
