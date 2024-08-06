package com.yannqing.yanoj.model.dto.question;

import lombok.Data;

/**
 * @description: 数据库对应字段的json类
 * @title: 题目配置
 * @author: yannqing
 * @create: 2024-07-21 15:59
 **/
@Data
public class JudgeConfig {


    /**
     * 时间限制（ms）
     */
    private Long timeLimit;
    /**
     * 内存限制（kb）
     */
    private Long memoryLimit;
    /**
     * 堆栈限制（kb）
     */
    private Long stackLimit;
}
