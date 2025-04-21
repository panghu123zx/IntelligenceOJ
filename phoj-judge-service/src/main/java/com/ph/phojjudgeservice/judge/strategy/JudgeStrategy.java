package com.ph.phojjudgeservice.judge.strategy;


import com.ph.phojmodel.model.codesandbox.JudgeContext;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import org.springframework.stereotype.Service;

@Service
public interface JudgeStrategy {

    /**
     * 执行判题逻辑
     * @param judgeContext
     * @return
     */
    judgeInfo doJudge(JudgeContext judgeContext);
}
