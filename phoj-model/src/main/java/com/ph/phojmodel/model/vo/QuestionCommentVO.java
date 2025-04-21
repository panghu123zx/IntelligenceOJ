package com.ph.phojmodel.model.vo;


import com.ph.phojmodel.model.entity.QuestionComment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class QuestionCommentVO implements Serializable {
    /**
     * 主键id
     */
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
     * 评论的子级评论
     */
    private List<QuestionCommentVO> commentVOChildList;


    /**
     * 包装类转对象
     *
     * @param questionCommentVO
     * @return
     */
    public static QuestionComment voToObj(QuestionCommentVO questionCommentVO) {
        if (questionCommentVO == null) {
            return null;
        }
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentVO, questionComment);

        return questionComment;
    }

    /**
     * 对象转包装类
     *
     * @param questionComment
     * @return
     */
    public static QuestionCommentVO objToVo(QuestionComment questionComment) {
        if (questionComment == null) {
            return null;
        }
        QuestionCommentVO QuestionCommentVO = new QuestionCommentVO();
        BeanUtils.copyProperties(questionComment, QuestionCommentVO);
        return QuestionCommentVO;
    }


}
