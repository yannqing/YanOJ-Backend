package com.yannqing.yanoj.model.dto.question;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 更新请求（管理员）
 *
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目提交数
     */
    private Integer submitnum;

    /**
     * 题目通过数
     */
    private Integer acceptednum;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgecase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeconfig;

    private static final long serialVersionUID = 1L;
}