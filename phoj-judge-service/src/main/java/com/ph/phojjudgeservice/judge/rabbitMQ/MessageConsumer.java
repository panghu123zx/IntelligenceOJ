package com.ph.phojjudgeservice.judge.rabbitMQ;


import cn.hutool.json.JSONUtil;
import com.ph.phojclient.service.QuestionClientService;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.constant.CommonConstant;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojjudgeservice.judge.JudgeServe;
import com.ph.phojjudgeservice.judge.manager.RedissonLock;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojmodel.model.enums.JudgeMessageEnum;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;


@Slf4j
@Component
public class MessageConsumer {

    @Resource
    private JudgeServe judgeServe;
    @Resource
    private QuestionClientService questionClientService;
    @Resource
    private RedissonLock redissonLock;

    /**
     * @param message     接收的信息
     * @param channel     通道，用于给RabbitMQ交互的，可以手动的拒绝合接收消息
     * @param deliveryTag 消息的投递标签，用于唯一标识的标签
     */
    @SneakyThrows  //异常的注解化简单处理
    @RabbitListener(queues = {CommonConstant.QUEUE_NAME}, ackMode = "MANUAL") //监听队列名为oj_queue，设置消息确认机制为手动接收
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            //消息为空拒绝消息进入死信队列
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long questionSubmitId = Long.parseLong(message);
        try {
            judgeServe.doJudgeServe(questionSubmitId);
            QuestionSubmit questionSubmit = questionClientService.getQuestionSubmitId(questionSubmitId);
            if (!Objects.equals(questionSubmit.getStatus(), SubmitStatusEnum.SUCCEED.getValue())) {
                //消息为空拒绝消息进入死信队列
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题失败");
            }
            log.info("提交的题目信息:{}", questionSubmit);
            //设置通过数，只有通过了才修改通过数
            Long questionId = questionSubmit.getQuestionId();
            Question question = questionClientService.getById(questionId);
            String judgeInfo = questionSubmit.getJudgeInfo();
            judgeInfo bean = JSONUtil.toBean(judgeInfo, judgeInfo.class);
            String res = bean.getMessage();
            if (res == null) {
                res=JudgeMessageEnum.SYSTEM_ERROR.getValue();
            }
            if (!Objects.equals(res, JudgeMessageEnum.ACCEPTED.getValue())) {
                //没有通过 提交数加1
                Integer submitNum = question.getSubmitNum();
                Question updateQuestion = new Question();
                submitNum = submitNum + 1;
                updateQuestion.setId(questionId);
                updateQuestion.setSubmitNum(submitNum);
                String lockKey = questionSubmitId + ":" + questionId + ":" + submitNum;
                Boolean update = redissonLock.lockExecute(lockKey, () -> questionClientService.updateQuestionById(updateQuestion));
                if (!update) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存数据失败");
                }

            } else {
                //通过 通过数和提交数都加1
                Integer acceptNum = question.getAcceptNum();
                Integer submitNum = question.getSubmitNum();
                Question updateQuestion = new Question();
                acceptNum = acceptNum + 1;
                submitNum = submitNum + 1;
                updateQuestion.setId(questionId);
                updateQuestion.setSubmitNum(submitNum);
                updateQuestion.setAcceptNum(acceptNum);
                //todo 加锁
                String lockKey = questionSubmitId + ":" + questionId + ":" + acceptNum + ":" + submitNum;
                Boolean update = redissonLock.lockExecute(lockKey, () -> questionClientService.updateQuestionById(updateQuestion));

                if (!update) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存数据失败");
                }
            }


            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

}
