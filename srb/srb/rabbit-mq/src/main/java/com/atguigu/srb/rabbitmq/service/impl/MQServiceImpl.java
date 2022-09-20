package com.atguigu.srb.rabbitmq.service.impl;

import com.atguigu.srb.rabbitmq.service.MQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/5 15:30 周二
 * description:
 */
@Service
public class MQServiceImpl implements MQService {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String exchange, String routing, Object object) {
        rabbitTemplate.convertAndSend(exchange, routing, object);
    }
}
