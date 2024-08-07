package com.yannqing.yanoj.judge.codesandbox;

import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @description: 代码沙箱接口
 * @author: yannqing
 * @create: 2024-08-06 17:16
 **/
public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
