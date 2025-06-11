package com.ftc.websocketdemo.handler;

import cn.hutool.core.util.ObjectUtil;
import com.ftc.websocketdemo.handler.enums.RoomHandlerTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 冯铁城 [17615007230@163.com]
 * @date 2025-06-11 15:16:16
 * @describe 消息处理器工厂
 */
@Component
public class MessageHandlerFactory implements ApplicationContextAware {

    /**
     * 处理器类型-消息处理器MAP
     */
    private static final ConcurrentHashMap<String, MessageHandler> HANDLER_MAP = new ConcurrentHashMap<>();

    /**
     * 获取消息处理器
     *
     * @param handlerType 处理器类型 {@link RoomHandlerTypeEnum}
     * @return 消息处理器
     */
    public static MessageHandler getHandler(String handlerType) {

        //1.获取处理器
        final MessageHandler handler = HANDLER_MAP.get(handlerType);

        //2.判空
        Assert.isTrue(ObjectUtil.isNotEmpty(handler), "can not get handler by type:" + handlerType);

        //3.返回
        return handler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //1.获取全部实现类
        final Map<String, MessageHandler> beansOfType = applicationContext.getBeansOfType(MessageHandler.class);

        //2.循环
        for (String className : beansOfType.keySet()) {

            //3.获取实现类以及type
            final MessageHandler handler = beansOfType.get(className);
            final String handlerType = handler.getHandlerType();

            //4.封装Map
            HANDLER_MAP.put(handlerType, handler);
        }
    }
}
