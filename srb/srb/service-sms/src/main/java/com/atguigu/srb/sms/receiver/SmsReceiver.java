package com.atguigu.srb.sms.receiver;

import com.atguigu.srb.base.dto.SmsDTO;
import com.atguigu.srb.rabbitmq.constant.MQConst;
import com.atguigu.srb.sms.service.SmsService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/5 14:38 周二
 * description:
 */
@Component
@Slf4j
public class SmsReceiver {
    @Autowired
    SmsService smsService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConst.QUEUE_SMS_ITEM,durable = "true"),
            exchange = @Exchange(value = MQConst.EXCHANGE_TOPIC_SMS),
            key = {MQConst.ROUTING_SMS_ITEM}
    ))
    public void a(Channel channel, Message message, SmsDTO smsDTO) throws IOException {
        byte[] body = message.getBody();
        String str = new String(body);
        System.out.println("消费段消费消息");
        smsService.sendRecharge(smsDTO);
        System.out.println("确认消费");
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);// 参数1投递状态，参数2是否批量确认
    }

}
