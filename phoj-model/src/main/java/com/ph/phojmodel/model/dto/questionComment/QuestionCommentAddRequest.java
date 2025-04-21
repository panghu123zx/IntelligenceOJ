package com.ph.phojmodel.model.dto.questionComment;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionCommentAddRequest implements Serializable {


    /**
     * 问题id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 回复人名称
     */
    private String fromName;


    /**
     * 回复条数
     */
    private Integer commentNum;

    /**
     * 父级评论id
     */
    private Long parentId;




    private static final long serialVersionUID = 1L;
}