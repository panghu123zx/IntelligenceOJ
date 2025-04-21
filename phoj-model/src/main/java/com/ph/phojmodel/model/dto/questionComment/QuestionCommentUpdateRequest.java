package com.ph.phojmodel.model.dto.questionComment;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionCommentUpdateRequest implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    private static final long serialVersionUID = 1L;
}