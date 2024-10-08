package com.yannqing.yanoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yannqing.yanoj.common.ErrorCode;
import com.yannqing.yanoj.constant.CommonConstant;
import com.yannqing.yanoj.exception.BusinessException;
import com.yannqing.yanoj.judge.JudgeService;
import com.yannqing.yanoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yannqing.yanoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yannqing.yanoj.model.entity.Question;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.yannqing.yanoj.model.entity.QuestionSubmit;
import com.yannqing.yanoj.model.entity.User;
import com.yannqing.yanoj.model.enums.QuestionSubmitLanguageEnum;
import com.yannqing.yanoj.model.enums.QuestionSubmitStatusEnum;
import com.yannqing.yanoj.model.vo.QuestionSubmitVO;
import com.yannqing.yanoj.service.QuestionService;
import com.yannqing.yanoj.service.QuestionSubmitService;
import com.yannqing.yanoj.service.QuestionSubmitService;
import com.yannqing.yanoj.mapper.QuestionSubmitMapper;
import com.yannqing.yanoj.service.UserService;
import com.yannqing.yanoj.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 67121
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-07-21 15:16:36
*/
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Resource
    private UserService userService;
    @Autowired
    private QuestionSubmitMapper questionSubmitMapper;

    /**
     * 提交题目
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
        long userId = loginUser.getId();
        String code = questionSubmitAddRequest.getCode();
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmit questionSubmit = new QuestionSubmit();
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
        Long questionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {

        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅管理员和自己能看到自己
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserid() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(QuestionSubmitQueryRequest questionSubmitQueryRequest, User loginUser) {

        // 1. 获取请求数据
        String language = questionSubmitQueryRequest.getLanguage();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        int current = questionSubmitQueryRequest.getCurrent();
        int pageSize = questionSubmitQueryRequest.getPageSize();

        // 2. 拼接查询条件 以及 按时间排序（倒序）
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(language)) {
            queryWrapper.eq("language", language);
        }
        if (questionId != null && questionId != 0) {
            queryWrapper.eq("questionId", questionId);
        }
        queryWrapper.orderBy(true, false, "createTime");

        // 3. 分页查询出来 Page<QuestionSubmit>
        Page<QuestionSubmit> questionSubmitPage= questionSubmitMapper.selectPage(new Page<>(current, pageSize), queryWrapper);

        // 4. 将 Page<QuestionSubmit> 转化为 Page<QuestionSubmitVO>, 并返回结果
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(current, pageSize);
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);

        return questionSubmitVOPage;
    }
}




