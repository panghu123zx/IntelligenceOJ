package com.ph.phojmodel.model.dto.QuestionAi;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionAiAddRequest implements Serializable {


    /**
     * ai提问的内容
     */
    private String genContent;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 提交 id
     */
    private Long questionSubmitId;



    private static final long serialVersionUID = 1L;
}