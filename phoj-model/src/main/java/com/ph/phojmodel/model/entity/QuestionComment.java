package com.ph.phojmodel.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论
 * @TableName question_comment
 */
@TableName(value ="question_comment")
@Data
public class QuestionComment implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 父级评论id
     */
    private Long parentId;

    /**
     * 回复条数
     */
    private Integer commentNum;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 是否显示输入框
     */
    private Integer inputShow;

    /**
     * 回复记录id
     */
    private Long fromId;

    /**
     * 回复人名称
     */
    private String fromName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除 1（true）已删除， 0（false）未删除
     */
    @TableLogic
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}