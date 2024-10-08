package com.achobeta.domain.interview.enums;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-05
 * Time: 23:18
 */
@Getter
@AllArgsConstructor
public enum InterviewStatus {

    NOT_STARTED(0, "未开始"),
    STARTING(1, "进行中"),
    ENDED(2, "已结束"),

    ;

    @EnumValue
    @JsonValue
    private final Integer status;

    private final String description;

    @Override
    public String toString() {
        return description;
    }

    public void check(List<InterviewStatus> interviewStatus) {
        if(CollectionUtils.isEmpty(interviewStatus) || !interviewStatus.contains(this)) {
            throw new GlobalServiceException(String.format("面试%s", this.getDescription()),
                    GlobalServiceStatusCode.INTERVIEW_STATUS_EXCEPTION);
        }
    }

    public static InterviewStatus get(Integer status) {
        for (InterviewStatus interviewStatus : InterviewStatus.values()) {
            if(interviewStatus.getStatus().equals(status)) {
                return interviewStatus;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_STATUS_EXCEPTION);
    }
}
