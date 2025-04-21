package com.ph.phojmodel.model.codesandbox;


import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文
 */
@Data
public class JudgeContext {
    /**
     * 示例输出数据
     */
    private List<String> output;
    /**
     * 题目提交信息
     */
    private judgeInfo judgeInfo;
    /**
     * 题目信息
     */
    private Question question;
    /**
     * 输出的数据
     */
    private List<String> resultOutput;

    /**
     * 题目提交的信息
     */
    private QuestionSubmit questionSubmit;
}
