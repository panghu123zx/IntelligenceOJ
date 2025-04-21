package com.ph.phojmodel.model.dto.QuestionAi;

import com.ph.phojcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionAiQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * ai提问的内容
     */
    private String genContent;


    /**
     * 题目 id
     */
    private Long questionId;

    private Long questionSubmitId;
    private Long userId;

    /**
     * 状态
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}