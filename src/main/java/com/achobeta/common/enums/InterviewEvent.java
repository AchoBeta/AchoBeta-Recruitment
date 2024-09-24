package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:56
 */
@Getter
@AllArgsConstructor
public enum InterviewEvent {

    INTERVIEW_START(1, "面试开始"),

    INTERVIEW_OVER(2, "面试结束"),

    INTERVIEW_START_AGAIN(3, "面试重新开始"),

    INTERVIEW_NOTICE(4, "面试通知"),

    INTERVIEW_SUMMARY(5, "面试总结"),

    INTERVIEW_EXPERIENCE(6, "面试经历"),

    ;


    private final Integer event;

    private final String description;

    @Override
    public String toString() {
        return description;
    }

    public static InterviewEvent get(Integer event) {
        for (InterviewEvent interviewEvent : InterviewEvent.values()) {
            if(interviewEvent.getEvent().equals(event)) {
                return interviewEvent;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_STATUS_TRANS_EVENT_ERROR);
    }
}
