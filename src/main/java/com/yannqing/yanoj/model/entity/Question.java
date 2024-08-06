package com.yannqing.yanoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
    private String tags;

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
     * 判题用例（json 数组）
     */
    private String judgecase;

    /**
     * 判题配置（json 对象）
     */
    private String judgeconfig;

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
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Question other = (Question) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getTags() == null ? other.getTags() == null : this.getTags().equals(other.getTags()))
            && (this.getAnswer() == null ? other.getAnswer() == null : this.getAnswer().equals(other.getAnswer()))
            && (this.getSubmitnum() == null ? other.getSubmitnum() == null : this.getSubmitnum().equals(other.getSubmitnum()))
            && (this.getAcceptednum() == null ? other.getAcceptednum() == null : this.getAcceptednum().equals(other.getAcceptednum()))
            && (this.getJudgecase() == null ? other.getJudgecase() == null : this.getJudgecase().equals(other.getJudgecase()))
            && (this.getJudgeconfig() == null ? other.getJudgeconfig() == null : this.getJudgeconfig().equals(other.getJudgeconfig()))
            && (this.getThumbnum() == null ? other.getThumbnum() == null : this.getThumbnum().equals(other.getThumbnum()))
            && (this.getFavournum() == null ? other.getFavournum() == null : this.getFavournum().equals(other.getFavournum()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getIsdelete() == null ? other.getIsdelete() == null : this.getIsdelete().equals(other.getIsdelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getTags() == null) ? 0 : getTags().hashCode());
        result = prime * result + ((getAnswer() == null) ? 0 : getAnswer().hashCode());
        result = prime * result + ((getSubmitnum() == null) ? 0 : getSubmitnum().hashCode());
        result = prime * result + ((getAcceptednum() == null) ? 0 : getAcceptednum().hashCode());
        result = prime * result + ((getJudgecase() == null) ? 0 : getJudgecase().hashCode());
        result = prime * result + ((getJudgeconfig() == null) ? 0 : getJudgeconfig().hashCode());
        result = prime * result + ((getThumbnum() == null) ? 0 : getThumbnum().hashCode());
        result = prime * result + ((getFavournum() == null) ? 0 : getFavournum().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getIsdelete() == null) ? 0 : getIsdelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", content=").append(content);
        sb.append(", tags=").append(tags);
        sb.append(", answer=").append(answer);
        sb.append(", submitnum=").append(submitnum);
        sb.append(", acceptednum=").append(acceptednum);
        sb.append(", judgecase=").append(judgecase);
        sb.append(", judgeconfig=").append(judgeconfig);
        sb.append(", thumbnum=").append(thumbnum);
        sb.append(", favournum=").append(favournum);
        sb.append(", userid=").append(userid);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", isdelete=").append(isdelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}