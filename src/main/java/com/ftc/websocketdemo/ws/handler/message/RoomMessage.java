package com.ftc.websocketdemo.ws.handler.message;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:52:24
 * @describe 房间消息
 */
@Data
public class RoomMessage {

    /**
     * 用户ID
     */
    private String userId = StrUtil.EMPTY;

    /**
     * 房间ID
     */
    private String roomId = StrUtil.EMPTY;
}
