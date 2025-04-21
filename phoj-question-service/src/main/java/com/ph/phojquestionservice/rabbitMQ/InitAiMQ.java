package com.ph.phojquestionservice.rabbitMQ;

import com.ph.phojcommon.constant.CommonConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Slf4j
public class InitAiMQ {
    /**
     * 题目提交的队列初始化
     */
    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @PostConstruct
    public void doQuestionInitMq(){
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            //创建连接
            Connection connection = factory.newConnection();
            //创建通道
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare("ai_exchange", "direct");
            //创建队列
            //声明队列                                          持久化    非独占    非自动删除      额外参数
            channel.queueDeclare("ai_queue", true, false, false, null);
            //队列绑定到交换机上，并且指定路由键 my_routing
            channel.queueBind("ai_queue" ,"ai_exchange", "ai_routing");
            log.info("初始化成功");
        } catch (Exception e) {
            log.error("初始化失败：{}",e.getMessage());
        }
    }

    /**
     * 死信队列
     */

    @PostConstruct
    public void doDeadInitMq(){
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            //创建连接
            Connection connection = factory.newConnection();
            //创建通道
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(CommonConstant.DLX_EXCHANGE, "direct");
            //创建队列
            //声明队列                                          持久化    非独占    非自动删除      额外参数
            channel.queueDeclare(CommonConstant.DLX_QUEUE, true, false, false, null);
            //队列绑定到交换机上，并且指定路由键 my_routing
            channel.queueBind(CommonConstant.DLX_QUEUE ,CommonConstant.DLX_EXCHANGE, CommonConstant.DLX_ROUTING);
            log.info("初始化成功");
        } catch (Exception e) {
            log.error("初始化失败：{}",e.getMessage());
        }
    }

}
