package com.ph.phojquestionservice.rabbitMQ;


import com.ph.phojcommon.constant.CommonConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息
 */
@Component
public class MessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * rabbitMq发送消息
     * @param message 路由规则
     */
    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(CommonConstant.EXCHANGE_NAME ,CommonConstant.ROUTING_KEY,message);
    }
}
