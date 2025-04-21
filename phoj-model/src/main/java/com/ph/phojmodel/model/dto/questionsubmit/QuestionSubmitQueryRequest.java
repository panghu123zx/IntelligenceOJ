package com.ph.phojmodel.model.dto.questionsubmit;


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
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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

    /**
     * 提交状态 0-待判题 1-判题中 2-成功 3-失败
     */
    private Integer status;



    private static final long serialVersionUID = 1L;
}