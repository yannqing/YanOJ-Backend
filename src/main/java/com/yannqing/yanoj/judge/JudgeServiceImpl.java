package com.yannqing.yanoj.judge;

import cn.hutool.json.JSONUtil;
import com.yannqing.yanoj.common.ErrorCode;
import com.yannqing.yanoj.exception.BusinessException;
import com.yannqing.yanoj.judge.codesandbox.CodeSandBox;
import com.yannqing.yanoj.judge.codesandbox.CodeSandBoxFactory;
import com.yannqing.yanoj.judge.codesandbox.CodeSandboxProxy;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yannqing.yanoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yannqing.yanoj.model.dto.question.JudgeCase;
import com.yannqing.yanoj.judge.codesandbox.model.JudgeInfo;
import com.yannqing.yanoj.model.entity.Question;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.yannqing.yanoj.model.enums.JudgeInfoMessageEnum;
import com.yannqing.yanoj.model.enums.QuestionSubmitStatusEnum;
import com.yannqing.yanoj.service.QuestionService;
import com.yannqing.yanoj.service.QuestionSubmitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 判题服务实现类
 * @author: yannqing
 * @create: 2024-08-07 10:03
 * @from: <更多资料：yannqing.com>
 **/
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1) 传入题目的提交 id，获取到对应的题目，提交信息（包含代码，编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionid();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3) 更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 4) 调用沙箱，获取到执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandboxProxy(codeSandBox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = question.getJudgecase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();

        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        // 使用 judgeManager 进行选择不同的策略（解耦）
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
//        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
//        JudgeInfo judgeInfo = judgeStrategy.doJudge(judgeContext);
        log.info("策略返回的判题信息{}", judgeInfo);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeinfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);

        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 修改题目的提交次数
        Question updateQuestion = new Question();
        updateQuestion.setId(questionId);
        updateQuestion.setSubmitnum(question.getSubmitnum() + 1);
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            updateQuestion.setAcceptednum(question.getAcceptednum() + 1);
        }
        update = questionService.updateById(updateQuestion);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交数量更新错误");
        }
        return questionSubmitService.getById(questionSubmitId);
    }
}
