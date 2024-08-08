package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-05
 * Time: 23:18
 */
@Getter
public enum InterviewStatusEnum {

    NOT_STARTED(0, "未开始"),
    STARTING(1, "进行中"),
    ENDED(2, "已结束"),

    ;

    @EnumValue
    @JsonValue
    private final Integer status;

    private final String description;

    InterviewStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public void check(InterviewStatusEnum interviewStatusEnum) {
        if(interviewStatusEnum != this) {
            throw new GlobalServiceException(String.format("面试%s", this.getDescription()),
                    GlobalServiceStatusCode.INTERVIEW_STATUS_EXCEPTION);
        }
    }

    public static InterviewStatusEnum get(Integer status) {
        for (InterviewStatusEnum interviewStatusEnum : InterviewStatusEnum.values()) {
            if(interviewStatusEnum.getStatus().equals(status)) {
                return interviewStatusEnum;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_STATUS_EXCEPTION);
    }
}
