package com.yannqing.yanoj.judge.codesandbox.impl;

import com.yannqing.yanoj.judge.codesandbox.CodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanoj.model.dto.questionsubmit.JudgeInfo;
import com.yannqing.yanoj.model.enums.JudgeInfoMessageEnum;
import com.yannqing.yanoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @description: 示例代码沙箱（跑通程序流程）
 * @author: yannqing
 * @create: 2024-08-06 17:37
 * @from: <更多资料：yannqing.com>
 **/
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
