package com.ph.phojmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 题目代码
     */
    private String code;


    private static final long serialVersionUID = 1L;
}