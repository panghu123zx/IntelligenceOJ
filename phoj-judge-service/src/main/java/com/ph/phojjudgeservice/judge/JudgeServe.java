package com.ph.phojjudgeservice.judge;


import com.ph.phojmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

@Service
public interface JudgeServe {

    QuestionSubmit doJudgeServe(long questionSubmitId);
}
