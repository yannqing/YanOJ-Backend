package com.yannqing.yanoj.service;

import com.yannqing.yanoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yannqing.yanoj.model.entity.User;

/**
* @author 67121
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-07-21 15:16:36
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录 id
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

}
