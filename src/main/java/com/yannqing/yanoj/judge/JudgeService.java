package com.yannqing.yanoj.judge;

import com.yannqing.yanoj.model.entity.QuestionSubmit;

/**
 * @description: 判题服务业务接口
 * @author: yannqing
 * @create: 2024-08-06 16:50
 * @from: <更多资料：yannqing.com>
 **/
public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);

}
