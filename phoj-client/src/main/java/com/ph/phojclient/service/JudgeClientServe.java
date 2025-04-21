package com.ph.phojclient.service;

import com.ph.phojmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="phoj-judge-service",path = "/api/judge/inner")
public interface JudgeClientServe {

    /**
     * 判题服务
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudgeServe(@RequestBody long questionSubmitId);
}
