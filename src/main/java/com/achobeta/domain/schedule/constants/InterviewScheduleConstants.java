package com.achobeta.domain.schedule.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-27
 * Time: 17:08
 */
public interface InterviewScheduleConstants {

    Integer MIN_GAP = 30;

    Integer MAX_GAP = 120;

    TimeUnit GAP_UNIT = TimeUnit.MINUTES;

    String SCHEDULE_EXIT_LOCK = "scheduleExitLock:";
    
}
