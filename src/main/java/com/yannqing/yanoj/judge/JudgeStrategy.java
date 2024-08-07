package com.yannqing.yanoj.judge;

import com.yannqing.yanoj.model.dto.questionsubmit.JudgeInfo;

/**
 * @description: 判题策略接口
 * @author: yannqing
 * @create: 2024-08-06 16:48
 **/
public interface JudgeStrategy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
