package com.yannqing.yanoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yannqing.yanoj.common.ErrorCode;
import com.yannqing.yanoj.exception.BusinessException;
import com.yannqing.yanoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yannqing.yanoj.model.entity.Question;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.yannqing.yanoj.model.entity.User;
import com.yannqing.yanoj.model.enums.QuestionSubmitLanguageEnum;
import com.yannqing.yanoj.service.QuestionService;
import com.yannqing.yanoj.service.QuestionSubmitService;
import com.yannqing.yanoj.service.QuestionSubmitService;
import com.yannqing.yanoj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 67121
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-07-21 15:16:36
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录 id
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //TODO 校验编程语言是否合法
        String language1 = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language1);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }


        Long questionid = questionSubmitAddRequest.getQuestionid();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionid);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        String code = questionSubmitAddRequest.getCode();
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setId(userId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        questionSubmit.setQuestionid(questionid);
        questionSubmit.setUserid(userId);

        //TODO 设置初始状态
        questionSubmit.setStatus(0);
        questionSubmit.setJudgeinfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return questionSubmit.getId();
    }

}




