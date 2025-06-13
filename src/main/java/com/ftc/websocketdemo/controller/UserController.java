package com.ftc.websocketdemo.controller;

import cn.hutool.core.util.IdUtil;
import com.ftc.websocketdemo.core.pool.user.UserPool;
import com.ftc.websocketdemo.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:43:24
 * @describe 用户控制类
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserPool userPool;

    @GetMapping
    public User login(String userName) {

        //1.随机生成用户ID
        String userId = IdUtil.fastSimpleUUID();

        //2.创建用户
        User user = new User(userId, userName);

        //3.存入用户池
        userPool.addUser(user);

        //4.返回用户
        return user;
    }
}
