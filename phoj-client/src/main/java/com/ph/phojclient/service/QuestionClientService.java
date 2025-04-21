package com.ph.phojclient.service;


import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author 杨志亮
* @description 针对表【question(题目表)】的数据库操作Service
* @createDate 2024-11-04 19:31:03
*/
@FeignClient(name = "phoj-question-service",path ="/api/question/inner")
public interface QuestionClientService {
    /**
     * 根据id获取题目信息
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getById(@RequestParam("questionId") Long questionId);


    /**
     * 更新题目信息
     * @param question
     * @return
     */
    @PostMapping("/update")
    boolean updateQuestionById(@RequestBody Question question);

    /**
     * 根据id获取题目提交信息
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/id")
    QuestionSubmit getQuestionSubmitId(@RequestParam("questionSubmitId") Long questionSubmitId);

    /**
     * 更细题目提交信息
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    Boolean updateById(@RequestBody QuestionSubmit questionSubmit);



}
