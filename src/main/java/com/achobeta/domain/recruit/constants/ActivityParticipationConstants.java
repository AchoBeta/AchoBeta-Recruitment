package com.achobeta.domain.recruit.constants;

import java.util.concurrent.TimeUnit;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-27
 * Time: 17:13
 */
public interface ActivityParticipationConstants {

    String DEFAULT_ANSWER = "";

    Integer MIN_GAP = 1;

    Integer MAX_GAP = 2;

    TimeUnit GAP_UNIT = TimeUnit.HOURS;

    String USER_PARTICIPATE_LOCK = "userParticipateLock:%d:%d";
    
}
