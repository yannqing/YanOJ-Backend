package com.yannqing.yanoj.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息消息
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum JudgeInfoMessageEnum {

    USER_AVATAR("用户头像", "user_avatar"),
    ACCEPTED("用户头像", "user_avatar"),
    WRONG_ANSWER("用户头像", "user_avatar"),
    COMPILE_ERROR("用户头像", "user_avatar"),
    MEMORY_LIMIT("用户头像", "user_avatar"),
    TIME_LIMIT("用户头像", "user_avatar"),
    PRESENTATION_ERROR("用户头像", "user_avatar"),
    OUTPUT_LIMIT_EXCEEDED("用户头像", "user_avatar"),
    WAITING("用户头像", "user_avatar"),
    DANGEROUS_OPERATION("用户头像", "user_avatar"),
    SYSTEM_ERROR("用户头像", "user_avatar");

    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
