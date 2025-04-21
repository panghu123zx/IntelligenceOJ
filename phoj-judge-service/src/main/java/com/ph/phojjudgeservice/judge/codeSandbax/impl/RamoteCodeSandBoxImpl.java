package com.ph.phojjudgeservice.judge.codeSandbax.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ph.phojjudgeservice.judge.codeSandbax.CodeSandBox;
import com.ph.phojmodel.model.codesandbox.RequestCode;
import com.ph.phojmodel.model.codesandbox.ResponseCode;
import org.springframework.stereotype.Component;


/**
 * 代码沙箱的实现类（远程代码沙箱）
 */
@Component
public class RamoteCodeSandBoxImpl implements CodeSandBox {

    @Override
    public ResponseCode executeCode(RequestCode requestCode) {
        System.out.println("远程代码沙箱");
        String url="http://101.37.170.234:8191/executeCode";
        String requestCodeStr = JSONUtil.toJsonStr(requestCode);
        String responseCodeStr = HttpUtil.createPost(url)
                .body(requestCodeStr)
                .execute()
                .body();
        ResponseCode responseCode = JSONUtil.toBean(responseCodeStr, ResponseCode.class);
        return responseCode;
    }
}
