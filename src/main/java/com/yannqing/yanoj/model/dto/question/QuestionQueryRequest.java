package com.yannqing.yanoj.model.dto.question;

import com.yannqing.yanoj.common.PageRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

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
     * 标签列表
     */
    private List<String> tags;

    /**
     * 答案
     */
    private String answer;

    /**
     * 收藏数
     */
    private Integer favournum;

    /**
     * 创建用户 id
     */
    private Long userid;

    private static final long serialVersionUID = 1L;
}