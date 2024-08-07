package com.yannqing.yanoj.judge.codesandbox.impl;

import com.yannqing.yanoj.judge.codesandbox.CodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @description: 第三方代码沙箱（调用网上线程的代码沙箱）
 * @author: yannqing
 * @create: 2024-08-06 17:37
 * @from: <更多资料：yannqing.com>
 **/
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");


        return null;
    }
}
