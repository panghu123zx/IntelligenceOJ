package com.ph.phojquestionservice.rabbitMQ;


import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojmodel.model.entity.QuestionAi;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import com.ph.phojquestionservice.manager.AIManager;
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


@Slf4j
@Component
public class AiConsumer {
    @Resource
    private QuestionAiService questionAiService;
    @Resource
    private AIManager aiManager;

    /**
     * @param message     接收的信息
     * @param channel     通道，用于给RabbitMQ交互的，可以手动的拒绝合接收消息
     * @param deliveryTag 消息的投递标签，用于唯一标识的标签
     */
    @SneakyThrows  //异常的注解化简单处理
    @RabbitListener(queues = {"ai_queue"}, ackMode = "MANUAL") //监听队列名为oj_queue，设置消息确认机制为手动接收
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long questionAiId = Long.parseLong(message);
        QuestionAi questionAi = questionAiService.getById(questionAiId);
        if (questionAi==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空");
        }
        QuestionAi updateQuestionAi = new QuestionAi();
        updateQuestionAi.setId(questionAi.getId());
        updateQuestionAi.setStatus(SubmitStatusEnum.RUNNING.getValue());
        boolean save = questionAiService.updateById(updateQuestionAi);
        if(!save){
            channel.basicNack(deliveryTag, false, false);
            handleQuestionAiStatus(questionAiId,"ai解析失败");
            return;
        }
        //调用ai接口
        String res = aiManager.SendMsgToAI( questionAi.getGenContent());
        QuestionAi updateQuestionAi2 = new QuestionAi();
        updateQuestionAi2.setStatus(SubmitStatusEnum.SUCCEED.getValue());
        updateQuestionAi2.setId(questionAiId);
        updateQuestionAi2.setGenRes(res);
        boolean update = questionAiService.updateById(updateQuestionAi2);
        if(!update){
            channel.basicNack(deliveryTag, false, false);
            handleQuestionAiStatus(questionAiId,"ai解析失败");
            return;
        }
        channel.basicAck(deliveryTag, false);

    }



    /**
     * 更新ai解析为失败
     * @param id
     * @param message
     */
    private void handleQuestionAiStatus(Long id,String message){
        QuestionAi questionAi = new QuestionAi();
        questionAi.setId(id);
        questionAi.setStatus(SubmitStatusEnum.FAILED.getValue());
        questionAi.setGenRes(message);
        boolean b = questionAiService.updateById(questionAi);
        if(!b){
            log.error("ai解析更新失败");
        }
    }
}
