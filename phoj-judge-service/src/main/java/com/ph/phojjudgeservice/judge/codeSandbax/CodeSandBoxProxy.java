package com.ph.phojjudgeservice.judge.codeSandbax;


import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandBoxProxy implements CodeSandBox {

    private CodeSandBox codeSandBox;

    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public ResponseCode executeCode(RequestCode requestCode) {
        ResponseCode responseCode = codeSandBox.executeCode(requestCode);
        return responseCode;
    }
}
