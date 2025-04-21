package com.ph.phojjudgeservice.judge;


import com.ph.phojjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.ph.phojjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.ph.phojjudgeservice.judge.strategy.JudgeStrategy;
import com.ph.phojmodel.model.codesandbox.JudgeContext;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManage {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    judgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
