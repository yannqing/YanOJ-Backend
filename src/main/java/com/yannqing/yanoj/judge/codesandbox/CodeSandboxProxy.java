package com.yannqing.yanoj.judge.codesandbox;

import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * 代码沙箱的代理模式（可以输出所有代码沙箱的请求和响应信息）
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandBox {

    private final CodeSandBox codeSandbox;


    public CodeSandboxProxy(CodeSandBox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：{}", executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：{}", executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
