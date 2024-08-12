package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-12
 * Time: 0:56
 */
@Getter
public enum InterviewStateEvent {

    INTERVIEW_START(1, "面试开始"),

    INTERVIEW_OVER(2, "面试结束"),

    INTERVIEW_START_AGAIN(3, "面试重新开始"),

    INTERVIEW_STARTING_NOTICE(4, "面试通知"),

    INTERVIEW_STARTING_SUMMARY(5, "面试总结"),

    ;


    private final Integer event;

    private final String description;

    InterviewStateEvent(Integer event, String description) {
        this.event = event;
        this.description = description;
    }

    public static InterviewStateEvent get(Integer event) {
        for (InterviewStateEvent interviewStateEvent : InterviewStateEvent.values()) {
            if(interviewStateEvent.getEvent().equals(event)) {
                return interviewStateEvent;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.INTERVIEW_STATUS_TRANS_EVENT_EXCEPTION);
    }
}
