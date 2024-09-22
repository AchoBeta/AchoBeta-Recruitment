package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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

    NEXT(1, "推进", "简历状态推进了一步"),

    APPROVE(2, "通过", "您已经通过了我们的招新评估！"),

    ELIMINATE(3, "不通过", "很遗憾，您未能通过我们的招新评估"),

    RESET(4, "重置", "简历状态已重置，您可以继续投递简历"),

    PENDING(5, "待处理", "我们已接受到您的反馈，会尽快处理"),

    SUSPEND(6, "挂起", "我们已接受到您的反馈，会尽快处理"),

    CONFIRM(7, "转正", "经过无数困难，您通过了我们的招新评估，成为了我们中的一员！"),

    ;

    @Override
    public String toString() {
        return description;
    }

    @EnumValue
    @JsonValue
    private final Integer event;

    private final String description;

    private final String remark;

    ResumeEvent(Integer event, String description, String remark) {
        this.event = event;
        this.description = description;
        this.remark = remark;
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
