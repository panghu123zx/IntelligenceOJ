package com.ph.phojjudgeservice.judge.rabbitMQ;

import com.ph.phojcommon.constant.CommonConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class InitMQ {
    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @PostConstruct
    public void doInitMq(){
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            //创建连接
            Connection connection = factory.newConnection();
            //创建通道
            Channel channel = connection.createChannel();
            //声明交换机
            channel.exchangeDeclare(CommonConstant.EXCHANGE_NAME, "direct");
            //创建队列
            //声明队列                                          持久化    非独占    非自动删除      额外参数
            channel.queueDeclare(CommonConstant.QUEUE_NAME, true, false, false, null);
            //队列绑定到交换机上，并且指定路由键 my_routing
            channel.queueBind(CommonConstant.QUEUE_NAME, CommonConstant.EXCHANGE_NAME, "oj_routing"); //
            log.info("初始化成功");
        } catch (Exception e) {
            log.error("初始化失败：{}",e.getMessage());
        }
    }

}
