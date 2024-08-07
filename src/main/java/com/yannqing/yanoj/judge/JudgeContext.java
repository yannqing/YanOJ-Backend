package com.yannqing.yanoj.judge;

import com.yannqing.yanoj.model.dto.question.JudgeCase;
import com.yannqing.yanoj.model.dto.questionsubmit.JudgeInfo;
import com.yannqing.yanoj.model.entity.Question;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: yannqing
 * @create: 2024-08-06 16:49
 * @from: <更多资料：yannqing.com>
 **/
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}

