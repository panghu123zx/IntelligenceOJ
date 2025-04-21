package com.ph.phojjudgeservice.judge.codeSandbax.impl;


import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBox;
import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;
import com.ph.phojmodel.model.dto.questionsubmit.judgeInfo;
import com.ph.phojmodel.model.enums.JudgeMessageEnum;

import java.util.List;

/**
 * 沙箱的实现类（示例代码沙箱）
 */
public class ExampleCodeSandBoxImpl implements CodeSandBox {
    @Override
    public ResponseCode executeCode(RequestCode requestCode) {
        List<String> input = requestCode.getInput();
        ResponseCode responseCode = new ResponseCode();
        responseCode.setOutput(input);
        responseCode.setMessage("执行测试成功");
        responseCode.setStatus(JudgeMessageEnum.ACCEPTED.getText());
        judgeInfo judgeInfo = new judgeInfo();
        judgeInfo.setMessage(JudgeMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        responseCode.setJudgeInfo(judgeInfo);
        return responseCode;
    }
}
