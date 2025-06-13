package com.ftc.websocketdemo.core.pool.user;

import com.ftc.websocketdemo.entity.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 14:33:23
 * @describe 用户池
 */
@Slf4j
@Component
public class UserPool {

    /**
     * 用户池
     */
    private static final Map<String, User> USER_POOL = new ConcurrentHashMap<>();

    /**
     * 添加用户
     *
     * @param user 用户
     */
    public void addUser(User user) {

        //1.房间池加入房间
        USER_POOL.put(user.getUserId(), user);

        //2.日志打印
        log.info("用户[{}]已创建", user);
    }

    /**
     * 获取用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    public User getUser(String userId) {
        return USER_POOL.get(userId);
    }
}

