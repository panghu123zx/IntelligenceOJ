package com.ph.phojquestionservice.rabbitMQ;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送消息
 */
@Component
public class AiProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * rabbitMq发送消息,题目提交的解析
     * @param message 路由规则
     */
    public void sendAiMessage(String message){
        rabbitTemplate.convertAndSend("ai_exchange" ,"ai_routing",message);
    }
}
