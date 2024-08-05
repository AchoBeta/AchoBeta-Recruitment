package com.achobeta.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
//- 0 - Draft
//- 1 - To be screened
//- 2 - Screening failed
//- 3- Preliminary examination to be arranged
//- 4 - Pending the first attempt
//- 5- Failed the first test
//- 6 - Passed the first test
//- 7 - Pending retest
//- 8 - To be scheduled for a retest
//- 9 - Retest Passed
//- 10 - Pending final test
//- 11- Final exam to be scheduled
//- 12 - Final test passed
//- 13 - Pending
//- 14 - Pending
@Getter
public enum ResumeStatusEnum {
    DRAFT("草稿",0),
    TO_BE_SCREENED("待筛选",1),
    PRELIMINARY_EXAMINATION_TO_BE_ARRANGED("待安排初试", 3),
    PENDING_FIRST_ATTEMPT("待初试", 4),
    FAILED_FIRST_TEST("初试失败", 5),
    PASSED_FIRST_TEST("初试通过", 6),
    PENDING_RETEST("待复试", 7),
    TO_BE_SCHEDULED_FOR_RETEST("待安排复试", 8),
    RETEST_PASSED("复试通过", 9),
    PENDING_FINAL_TEST("待终试", 10),
    FINAL_EXAM_TO_BE_SCHEDULED("待安排终试", 11),
    FINAL_TEST_PASSED("终试通过", 12),
    ;

    ResumeStatusEnum(String message, Integer resumeStatusCode) {
        this.message = message;
        this.resumeStatusCode = resumeStatusCode;
    }

    private final String message;
    private final Integer resumeStatusCode;

}
