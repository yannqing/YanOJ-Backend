package com.yannqing.yanoj.model.dto.questionsubmit;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.yannqing.yanoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description: 查询请求
 * @author: yannqing
 * @create: 2024-08-24 15:33
 * @from: <更多资料：yannqing.com>
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
