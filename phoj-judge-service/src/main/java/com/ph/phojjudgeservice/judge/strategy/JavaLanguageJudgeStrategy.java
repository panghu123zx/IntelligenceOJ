package com.ph.phojjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.ph.phojmodel.model.codesandbox.JudgeContext;
import com.ph.phojmodel.model.dto.question.judgeConfig;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.enums.JudgeMessageEnum;

import java.util.List;
import java.util.Optional;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public judgeInfo doJudge(JudgeContext judgeContext) {
        List<String> output = judgeContext.getOutput();
        judgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Question question = judgeContext.getQuestion();
        List<String> resultOutput = judgeContext.getResultOutput();

        //获取时间
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        judgeInfo judgeInfoResponse = new judgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        //判断逻辑
        JudgeMessageEnum judgeMessageEnum = null;
        //判断结果的输出用例是否和预期的输出用例数量一样
        if (output.size() != resultOutput.size()) {
            judgeMessageEnum = JudgeMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //判断结果的每一项是否和预期输出的每一项一至
        for (int i = 0; i < resultOutput.size(); i++) {
            if (!output.get(i).equals(resultOutput.get(i))) {
                judgeMessageEnum = JudgeMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        //得到题目的判题配置
        String judgeConfigStr = question.getJudgeConfig();
        judgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, judgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        //判断题目限制是否符合要求
        //java程序需要额外执行10s
        Long JAVA_TIME = 10000L;
        if (time - JAVA_TIME > timeLimit) {
            judgeMessageEnum = JudgeMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (memory > memoryLimit) {
            judgeMessageEnum = JudgeMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(JudgeMessageEnum.ACCEPTED.getValue());
        return judgeInfoResponse;
    }
}
