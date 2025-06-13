package com.ftc.websocketdemo.controller;

import com.ftc.websocketdemo.core.pool.room.RoomPool;
import com.ftc.websocketdemo.entity.room.Room;
import com.ftc.websocketdemo.entity.room.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Room createRoom(RoomDto roomDto) {
        Room room = new Room(roomName);
        roomPool.addRoom(room);
        return room;
    }
}
