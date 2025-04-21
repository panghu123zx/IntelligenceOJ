package com.ph.phojquestionservice.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ph.phojmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.ph.phojmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojmodel.model.entity.User;
import com.ph.phojmodel.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 杨志亮
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-11-04 19:31:03
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    /**
     * 将题目信息封装到QuestionSubmitVo中
     * @param request
     * @param questionSubmit
     * @param questionSubmitVO
     */
    void obtainQuestionVO(HttpServletRequest request, QuestionSubmit questionSubmit, QuestionSubmitVO questionSubmitVO);
}
