package com.ftc.websocketdemo.entity.room;

import lombok.Data;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-13 16:16:09
 * @describe 房间数据传输对象
 */
@Data
public class RoomDto {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;
}
