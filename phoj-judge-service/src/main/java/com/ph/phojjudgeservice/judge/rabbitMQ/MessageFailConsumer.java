package com.ph.phojjudgeservice.judge.rabbitMQ;

import com.ph.phojclient.service.QuestionClientService;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.constant.CommonConstant;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import javax.annotation.Resource;

/**
 * 死信消费者
 */
@Component
@Slf4j
public class MessageFailConsumer {
    @Resource
    private QuestionClientService questionClientService;

    /**
     * 监听死信队列
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = {CommonConstant.DLX_QUEUE},ackMode = "MANUAL")
    public void receiveFailMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        log.info("死信队列接收到的消息:{}", message);
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        long questionSubmitId = Long.parseLong(message);
        QuestionSubmit questionSubmit = questionClientService.getQuestionSubmitId(questionSubmitId);
        if (questionSubmit == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提交题目信息不存在");
        }
        //更新题目信息为失败
        questionSubmit.setStatus(SubmitStatusEnum.FAILED.getValue());
        Boolean update = questionClientService.updateById(questionSubmit);
        if (!update) {
            log.info("处理死信队列信息失败，对应的题目提交的id为:{}", questionSubmitId);
            channel.basicNack(deliveryTag, false, false);
        }
        channel.basicAck(deliveryTag,false);
    }
}
