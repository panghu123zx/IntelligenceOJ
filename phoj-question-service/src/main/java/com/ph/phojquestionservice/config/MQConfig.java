package com.ph.phojquestionservice.config;

import com.ph.phojcommon.constant.CommonConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 死信交换机
 */
@Configuration
public class MQConfig {
    /**
     * 声明判题交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange1(){
        return new DirectExchange(CommonConstant.EXCHANGE_NAME);
    }

    /**
     * 判题队列也绑定到死信交换机上
     * @return
     */
    @Bean
    public Queue directQueue1(){
        Queue queue = new Queue(CommonConstant.QUEUE_NAME);
        queue.addArgument("x-dead-letter-exchange",CommonConstant.DLX_EXCHANGE);
        queue.addArgument("x-dead-letter-routing-key",CommonConstant.DLX_ROUTING);
        return  queue;
    }
    /**
     * 声明ai交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange2(){
        return new DirectExchange("ai_exchange");
    }

    /**
     * ai队列也绑定到死信交换机上
     * @return
     */
    @Bean
    public Queue directQueue2(){
        Queue queue = new Queue("ai_queue");
        queue.addArgument("x-dead-letter-exchange",CommonConstant.DLX_EXCHANGE);
        queue.addArgument("x-dead-letter-routing-key",CommonConstant.DLX_ROUTING);
        return  queue;
    }

    /**
     * 声明死信交换机
     * @return
     */
    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange(CommonConstant.DLX_EXCHANGE);
    }

    /**
     * 声明死信队列
     * @return
     */
    @Bean
    public Queue dlxQueue(){
        return  new Queue(CommonConstant.DLX_QUEUE);
    }

    /**
     * oj
     * 把普通交换机和死信队列绑定
     * @param directQueue1
     * @param directExchange1
     * @return
     */
    @Bean
    public Binding bindingQueueWithDlx(Queue directQueue1, DirectExchange directExchange1){
        return BindingBuilder.bind(directQueue1).to(directExchange1).with(CommonConstant.DLX_ROUTING);
    }

    /**
     * ai
     * @param directQueue2
     * @param directExchange2
     * @return
     */
    @Bean
    public Binding bindingAiQueueWithDlx(Queue directQueue2, DirectExchange directExchange2){
        return BindingBuilder.bind(directQueue2).to(directExchange2).with(CommonConstant.DLX_ROUTING);
    }

    /**
     * 绑定死信交换机和死信队列
     * @param dlxQueue
     * @param dlxExchange
     * @return
     */
    @Bean
    public Binding dlxBinding(Queue dlxQueue,DirectExchange dlxExchange){
        return  BindingBuilder.bind(dlxQueue).to(dlxExchange).with(CommonConstant.DLX_ROUTING);
    }
}
