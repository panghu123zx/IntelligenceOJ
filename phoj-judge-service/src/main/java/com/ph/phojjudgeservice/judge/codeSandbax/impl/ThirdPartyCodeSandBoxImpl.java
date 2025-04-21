package com.ph.phojjudgeservice.judge.codeSandbax.impl;


import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBox;
import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;

/**
 * 代码沙箱的实现类（第三方代码沙箱）
 */
public class ThirdPartyCodeSandBoxImpl implements CodeSandBox {
    @Override
    public ResponseCode executeCode(RequestCode requestCode) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
