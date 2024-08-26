package com.yannqing.yanoj.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增题目提交请求DTO
 *
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {


    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionid;

    private static final long serialVersionUID = 1L;
}