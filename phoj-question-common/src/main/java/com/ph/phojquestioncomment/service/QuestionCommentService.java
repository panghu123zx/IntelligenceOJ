package com.ph.phojquestioncomment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentAddRequest;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentDeleteRequest;
import com.ph.phojmodel.model.entity.QuestionComment;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojmodel.model.vo.QuestionCommentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 杨志亮
* @description 针对表【question_comment(评论)】的数据库操作Service
* @createDate 2024-11-24 17:49:43
*/
public interface QuestionCommentService extends IService<QuestionComment> {

    /**
     * 根据题目id获取到所有的评论
     * @param questionId
     * @return
     */
    List<QuestionCommentVO> getAllQuestionComment(long questionId);

    /**
     * 删除用户评论的一条内容
     * @param questionCommentDeleteRequest
     * @param loginUser
     * @return
     */
    boolean deleteCommentById(QuestionCommentDeleteRequest questionCommentDeleteRequest, User loginUser);


    /**
     * 添加评论
     * @param questionCommentAddRequest
     * @param request
     * @return
     */
    boolean addComment(QuestionCommentAddRequest questionCommentAddRequest, HttpServletRequest request);


    /**
     * 修改评论
     * @param questionComment
     * @return
     */
    boolean updateComment(QuestionComment questionComment);
}
