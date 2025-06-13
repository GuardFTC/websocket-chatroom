package com.ftc.websocketdemo.controller;

import com.ftc.websocketdemo.core.factory.RoomFactory;
import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.entity.room.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:43:24
 * @describe 房间控制类
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rooms")
public class RoomController {

    private final RoomPool roomPool;

    @GetMapping
    public List<Room> getAllRoom() {
        return roomPool.getAllRoom();
    }

    @PostMapping
    public Room createRoom(@RequestBody RoomDto roomDto) {

        //1.通过用户ID获取房间
        Room room = roomPool.getRoomByUserId(roomDto.getUserId());

        //2.如果已经是房主，那么直接返回
        if (room != null && room.getOwnerUserId().equals(roomDto.getUserId())) {
            return room;
        }

        //3.创建房间
        room = RoomFactory.createRoom(roomDto.getUserId(), roomDto.getRoomName());

        //5.房间池处理
        roomPool.addRoom(room);

        //6.返回房间
        return room;
    }
}
