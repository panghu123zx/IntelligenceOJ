package com.ph.phojquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.ph.phojquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.ph")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ph.phojclient.service"})
public class PhojQuestionServiceApplication {

    public static void main(String[] args) {
        //初始化队列
//        InitAiMQ.doQuestionInitMq();
//        InitAiMQ.doDeadInitMq();
        SpringApplication.run(PhojQuestionServiceApplication.class, args);
    }

}
