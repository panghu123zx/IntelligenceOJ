package com.ph.phojjudgeservice.judge.codeSandbax;


import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;

public interface CodeSandBox {
    /**
     * 代码沙箱接口
     * @param requestCode
     * @return
     */
    ResponseCode executeCode(RequestCode requestCode);
}
