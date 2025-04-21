package com.ph.phojjudgeservice.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.ph")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ph.phojclient.service"})
public class PhojJudgeServiceApplication {

    public static void main(String[] args) {
        //rabbitMQ初始化
//        InitMQ.doInitMq();
        SpringApplication.run(PhojJudgeServiceApplication.class, args);
    }

}
