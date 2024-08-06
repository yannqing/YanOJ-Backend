package com.yannqing.yanoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.yannqing.yanoj.model.dto.question.JudgeConfig;
import com.yannqing.yanoj.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.BeanUtils;

/**
 * 帖子视图
 *
 */
@Data
public class QuestionVO implements Serializable {
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
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeconfig;

    /**
     * 点赞数
     */
    private Integer thumbnum;

    /**
     * 收藏数
     */
    private Integer favournum;

    /**
     * 创建用户 id
     */
    private Long userid;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }

        JudgeConfig judgeconfig = questionVO.getJudgeconfig();
        question.setJudgeconfig(JSONUtil.toJsonStr(judgeconfig));
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);

        String judgeconfig = question.getJudgeconfig();
        questionVO.setJudgeconfig(JSONUtil.toBean(judgeconfig, JudgeConfig.class));

        return questionVO;
    }
}
