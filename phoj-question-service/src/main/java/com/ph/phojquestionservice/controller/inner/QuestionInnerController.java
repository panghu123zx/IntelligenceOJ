package com.ph.phojquestionservice.controller.inner;

import com.ph.phojclient.service.QuestionClientService;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojquestionservice.service.QuestionService;
import com.ph.phojquestionservice.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 内部接口，只能内部使用
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionClientService {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;

    /**
     * 根据id获取题目信息
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public Question getById(@RequestParam("questionId") Long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 更新题目信息
     * @param question
     * @return
     */
    @Override
    @PostMapping("/update")
    public boolean updateQuestionById(@RequestBody  Question question){
        return questionService.updateById(question);
    }

    /**
     * 根据id获取题目提交信息
     * @param questionSubmitId
     * @return
     */
    @Override
    @GetMapping("/question_submit/id")
    public QuestionSubmit getQuestionSubmitId(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public Boolean updateById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
