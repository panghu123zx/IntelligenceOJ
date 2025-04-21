package com.ph.phojjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.ph.phojclient.service.QuestionClientService;
import com.ph.phojcommon.common.ErrorCode;
import com.ph.phojcommon.exception.BusinessException;
import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBox;
import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBoxFactory;
import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBoxProxy;
import com.ph.phojmodel.model.codesandbox.JudgeContext;
import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;
import com.ph.phojmodel.model.dto.question.judgeCase;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.entity.Question;
import com.ph.phojmodel.model.entity.QuestionSubmit;
import com.ph.phojmodel.model.enums.SubmitStatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServeImpl implements JudgeServe {
    @Value("${codesandbox.type:Remote}")
    private String type;
    @Resource
    private QuestionClientService questionClientService;

    @Resource
    private JudgeManage judgeManage;



    @Override
    public QuestionSubmit doJudgeServe(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionClientService.getQuestionSubmitId(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交为空");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionClientService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息为空");
        }
        //如果题目状态为等待中，就不用重复执行
        Integer status = questionSubmit.getStatus();
        if (!status.equals(SubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "正在判题中, 请稍后");
        }
        //更新题目状态为判题中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(SubmitStatusEnum.RUNNING.getValue());
        boolean update = questionClientService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        //得到判题用例转化为 judgeCase对象 再获取到输入对象
        String judgeCaseStr = question.getJudgeCase();
        List<judgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, judgeCase.class);
        List<String> input = judgeCaseList.stream().map(judgeCase::getInput).collect(Collectors.toList());
        //得到 示例 输出用例
        List<String> output = judgeCaseList.stream().map(judgeCase::getOutput).collect(Collectors.toList());
        //调用代码沙箱，得到执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        RequestCode requestCode = RequestCode.builder()
                .code(code)
                .language(language)
                .input(input)
                .build();
        ResponseCode responseCode = codeSandBox.executeCode(requestCode);

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setOutput(output);
        judgeContext.setJudgeInfo(responseCode.getJudgeInfo());
        judgeContext.setQuestion(question);
        judgeContext.setResultOutput(responseCode.getOutput());
        judgeContext.setQuestionSubmit(questionSubmit);
        //题目判断业务
        judgeInfo judgeInfoResponse = judgeManage.doJudge(judgeContext);


        //最后更改题目提交信息信息
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(SubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResponse));
        update = questionClientService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        return questionClientService.getQuestionSubmitId(questionSubmitId);
    }
}
