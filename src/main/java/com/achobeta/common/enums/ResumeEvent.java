package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import lombok.Getter;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 1:29
 */
@Getter
public enum ResumeEvent {

    NEXT(1, "推进"),

    APPROVE(2, "通过"),

    ELIMINATE(3, "淘汰"),

    RESET(4, "重置"),

    PENDING(5, "待处理"),

    SUSPEND(6, "挂起"),

    CONFIRM(7, "转正"),

    ;

    @Override
    public String toString() {
        return description;
    }

    private final Integer event;

    private final String description;

    ResumeEvent(Integer event, String description) {
        this.event = event;
        this.description = description;
    }

    public static ResumeEvent get(Integer event) {
        for (ResumeEvent resumeEvent : ResumeEvent.values()) {
            if(resumeEvent.getEvent().equals(event)) {
                return resumeEvent;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_TRANS_EVENT_ERROR);
    }

}
