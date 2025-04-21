package com.ph.phojjudgeservice.judge.controller.inner;

import com.ph.phojclient.service.JudgeClientServe;
import com.ph.phojjudgeservice.judge.JudgeServe;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 内部接口，只能内部使用
 */

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeClientServe {
    @Resource
    private JudgeServe judgeServe;


    /**
     * 判题服务
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudgeServe(@RequestBody long questionSubmitId) {
        return judgeServe.doJudgeServe(questionSubmitId);
    }
}
