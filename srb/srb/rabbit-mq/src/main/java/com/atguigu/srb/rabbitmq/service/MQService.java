package com.atguigu.srb.rabbitmq.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/5 14:20 周二
 * description:
 */

public interface MQService {
    //    @Autowired
//    private AmqpTemplate amqpTemplate;
//
//    public boolean sendMessage(String exchange, String routingKey, Object message) {
//        log.info("发送消息");
//        amqpTemplate.convertAndSend(exchange, routingKey, message);
//        return true;
//    }
    void sendMessage(String exchange, String routing, Object object);
}
