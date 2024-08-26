package com.yannqing.yanoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yannqing.yanoj.judge.JudgeContext;
import com.yannqing.yanoj.judge.JudgeStrategy;
import com.yannqing.yanoj.model.dto.question.JudgeCase;
import com.yannqing.yanoj.model.dto.question.JudgeConfig;
import com.yannqing.yanoj.judge.codesandbox.model.JudgeInfo;
import com.yannqing.yanoj.model.entity.Question;
import com.yannqing.yanoj.model.enums.JudgeInfoMessageEnum;

/**
 * @description:
 * @author: yannqing
 * @create: 2024-08-06 16:51
 * @from: <更多资料：yannqing.com>
 **/
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        JudgeInfo judgeResponseInfo = new JudgeInfo();

        judgeResponseInfo.setMemory(memory);
        judgeResponseInfo.setTime(time);
        Question question = judgeContext.getQuestion();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (judgeContext.getOutputList().size() != judgeContext.getInputList().size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeResponseInfo.setMessage(judgeInfoMessageEnum.getValue());
            return judgeResponseInfo;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeContext.getJudgeCaseList().size(); i++) {
            JudgeCase judgeCase = judgeContext.getJudgeCaseList().get(i);
            if (!judgeCase.getOutput().equals(judgeContext.getOutputList().get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;;
                judgeResponseInfo.setMessage(judgeInfoMessageEnum.getValue());
                return judgeResponseInfo;
            }
        }
        // 判断题目限制

        String judgeConfigStr = question.getJudgeconfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needTimeLimit = judgeConfig.getTimeLimit();
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeResponseInfo.setMessage(judgeInfoMessageEnum.getValue());
            return judgeResponseInfo;
        }
        if (time > needTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeResponseInfo.setMessage(judgeInfoMessageEnum.getValue());
            return judgeResponseInfo;
        }
        judgeResponseInfo.setMessage(judgeInfoMessageEnum.getValue());
        return judgeResponseInfo;
    }
}
