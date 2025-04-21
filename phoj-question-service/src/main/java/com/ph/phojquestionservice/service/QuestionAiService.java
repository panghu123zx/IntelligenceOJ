package com.ph.phojquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ph.phojmodel.model.dto.QuestionAi.QuestionAiQueryRequest;
import com.ph.phojmodel.model.entity.QuestionAi;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 杨志亮
* @description 针对表【question_ai(题目表)】的数据库操作Service
* @createDate 2024-11-24 23:01:21
*/
public interface QuestionAiService extends IService<QuestionAi> {


    /**
     * 获取查询条件
     * @param questionAiQueryRequest
     * @return
     */
    Wrapper<QuestionAi> getQueryWrapper(QuestionAiQueryRequest questionAiQueryRequest);

}
