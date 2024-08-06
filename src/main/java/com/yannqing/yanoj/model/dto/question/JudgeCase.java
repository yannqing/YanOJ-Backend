package com.yannqing.yanoj.model.dto.question;

import lombok.Data;

/**
 * @description: 数据库对应字段的json类
 * @title: 题目用例
 * @author: yannqing
 * @create: 2024-07-21 15:59
 **/
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}
