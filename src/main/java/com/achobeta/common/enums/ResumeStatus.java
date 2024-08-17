package com.achobeta.common.enums;

import com.achobeta.exception.GlobalServiceException;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


@Getter
public enum ResumeStatus {
    DRAFT("草稿", 0),

    PENDING_SELECTION("待筛选", 1),
    REJECTED("筛选不通过", 2),

    SCHEDULE_INITIAL_INTERVIEW("待安排初试", 3),
    PENDING_INITIAL_INTERVIEW("待初试", 4),
    INITIAL_INTERVIEW_PASSED("初试通过", 5), // 仅当初试为最后一个流程时显示
    INITIAL_INTERVIEW_FAILED("初试不通过", 6), // 仅当初试为最后一个流程时显示

    SCHEDULE_SECOND_INTERVIEW("待安排复试", 7),
    PENDING_SECOND_INTERVIEW("待复试", 8),
    SECOND_INTERVIEW_PASSED("复试通过", 9), // 仅当复试为最后一个流程时显示
    SECOND_INTERVIEW_FAILED("复试不通过", 10), // 仅当复试为最后一个流程时显示

    SCHEDULE_FINAL_INTERVIEW("待安排终试", 11),
    PENDING_FINAL_INTERVIEW("待终试", 12),
    FINAL_INTERVIEW_PASSED("终试通过", 13), // 仅当复试为最后一个流程时显示
    FINAL_INTERVIEW_FAILED("终试不通过", 14), // 仅当复试为最后一个流程时显示

    PENDING_HANDLING("待处理", 15),
    SUSPENDED("挂起", 16),

    ;

    ResumeStatus(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    private final String message;

    @EnumValue
    @JsonValue
    private final Integer code;

    public ResumeStatus next() {
        return switch (this) {
            case DRAFT -> PENDING_SELECTION;
            case PENDING_SELECTION -> SCHEDULE_INITIAL_INTERVIEW;
            case SCHEDULE_INITIAL_INTERVIEW -> PENDING_INITIAL_INTERVIEW;
            case PENDING_INITIAL_INTERVIEW -> SCHEDULE_SECOND_INTERVIEW;
            case SCHEDULE_SECOND_INTERVIEW -> PENDING_SECOND_INTERVIEW;
            case PENDING_SECOND_INTERVIEW -> SCHEDULE_FINAL_INTERVIEW;
            case SCHEDULE_FINAL_INTERVIEW -> PENDING_FINAL_INTERVIEW;
            default -> throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_EXCEPTION);
        };
    }

    public ResumeStatus approve() {
        return switch (this) {
            case PENDING_INITIAL_INTERVIEW -> INITIAL_INTERVIEW_PASSED;
            case PENDING_SECOND_INTERVIEW -> SECOND_INTERVIEW_PASSED;
            case PENDING_FINAL_INTERVIEW -> FINAL_INTERVIEW_PASSED;
            default -> throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_EXCEPTION);
        };
    }

    public ResumeStatus eliminate() {
        return switch (this) {
            case PENDING_SELECTION -> REJECTED;
            case PENDING_INITIAL_INTERVIEW -> INITIAL_INTERVIEW_FAILED;
            case PENDING_SECOND_INTERVIEW -> SECOND_INTERVIEW_FAILED;
            case PENDING_FINAL_INTERVIEW -> FINAL_INTERVIEW_FAILED;
            default -> throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_EXCEPTION);
        };
    }

    public static ResumeStatus get(Integer code) {
        for (ResumeStatus resumeStatus : ResumeStatus.values()) {
            if(resumeStatus.getCode().equals(code)) {
                return resumeStatus;
            }
        }
        throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_STATUS_EXCEPTION);
    }
}
