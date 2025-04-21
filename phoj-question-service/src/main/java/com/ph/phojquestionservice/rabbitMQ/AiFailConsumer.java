package com.ph.phojquestionservice.rabbitMQ;

import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.constant.CommonConstant;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojmodel.model.entity.QuestionAi;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import com.ph.phojquestionservice.service.QuestionAiService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 死信消费者
 */
@Component
@Slf4j
public class AiFailConsumer {
    @Resource
    private QuestionAiService questionService;

    /**
     * 监听死信队列
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {CommonConstant.DLX_QUEUE}, ackMode = "MANUAL")
    public void receiveFailMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        log.info("死信队列接收到的消息:{}", message);
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        long questionAiId = Long.parseLong(message);
        QuestionAi questionAi = questionService.getById(questionAiId);
        if(questionAi==null){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "AI信息不存在");
        }
        questionAi.setStatus(SubmitStatusEnum.FAILED.getValue());
        boolean update = questionService.updateById(questionAi);
        if (!update) {
            log.info("处理死信队列信息失败，对应的ai的id为:{}", questionAiId);
            channel.basicNack(deliveryTag, false, false);
        }
        channel.basicAck(deliveryTag,false);
    }
}
