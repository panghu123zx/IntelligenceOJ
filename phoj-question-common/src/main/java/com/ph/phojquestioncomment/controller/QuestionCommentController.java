package com.ph.phojquestioncomment.controller;


import com.ph.phojclient.service.UserClientService;
import com.ph.phojcommon.common.BaseResponse;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.common.ResultUtils;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentAddRequest;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentDeleteRequest;
import com.ph.phojmodel.model.dto.questionComment.QuestionCommentUpdateRequest;
import com.ph.phojmodel.model.entity.QuestionComment;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojmodel.model.vo.QuestionCommentVO;
import com.ph.phojquestioncomment.service.QuestionCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/")
@Slf4j
public class QuestionCommentController {

    @Resource
    private QuestionCommentService questionCommentService;

    @Resource
    private UserClientService userClientService;

    /**
     * 获取到所有的评论信息
     *
     * @param questionId
     * @param request
     * @return
     */
    @GetMapping("/get/all")
    public BaseResponse<List<QuestionCommentVO>> getAllComment(long questionId, HttpServletRequest request) {
        if (questionId < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目为空");
        }
        return ResultUtils.success(questionCommentService.getAllQuestionComment(questionId));
    }

    /**
     * 删除用户评论
     *
     * @param questionCommentDeleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody QuestionCommentDeleteRequest questionCommentDeleteRequest, HttpServletRequest request) {
        User loginUser = userClientService.getLoginUser(request);
        if (!questionCommentDeleteRequest.getUserId().equals(loginUser.getId()) && !userClientService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "你无权删除该条评论");
        }
        return ResultUtils.success(questionCommentService.deleteCommentById(questionCommentDeleteRequest, loginUser));
    }

    /**
     * 添加评论
     *
     * @param questionCommentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addComment(@RequestBody QuestionCommentAddRequest questionCommentAddRequest, HttpServletRequest request) {
        if (questionCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(questionCommentService.addComment(questionCommentAddRequest, request));
    }

    /**
     *更新评论信息
     * @param questionCommentUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateComment(@RequestBody QuestionCommentUpdateRequest questionCommentUpdateRequest, HttpServletRequest request) {
        if (questionCommentUpdateRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(questionCommentUpdateRequest, questionComment);
        return ResultUtils.success(questionCommentService.updateComment(questionComment));
    }

}
