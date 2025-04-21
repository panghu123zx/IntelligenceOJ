package com.ph.phojjudgeservice.judge.codeSandbax;


import com.ph.phojjudgeservice.judge.codeSandbax.impl.ExampleCodeSandBoxImpl;
import com.ph.phojjudgeservice.judge.codeSandbax.impl.RamoteCodeSandBoxImpl;
import com.ph.phojjudgeservice.judge.codeSandbax.impl.ThirdPartyCodeSandBoxImpl;

/**
 * 代码沙箱工厂
 */
public class CodeSandBoxFactory {
    /**
     * 创建代码沙箱的实例
     * @param type 代码沙箱的类型
     * @return
     */
    public static CodeSandBox newInstance(String type){
        switch (type){
            case "example":
                return new ExampleCodeSandBoxImpl();
            case "Remote":
                return new RamoteCodeSandBoxImpl();
            case "thirdparty":
                return new ThirdPartyCodeSandBoxImpl();
            default:
                return new ExampleCodeSandBoxImpl();
        }
    }
}
