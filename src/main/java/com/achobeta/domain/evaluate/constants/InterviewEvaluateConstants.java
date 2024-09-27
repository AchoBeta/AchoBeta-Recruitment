package com.achobeta.domain.evaluate.constants;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-29
 * Time: 23:51
 */
public interface InterviewEvaluateConstants {

    int MIN_ABILITY_VALUE = 0;

    int MAX_ABILITY_VALUE = 5;

    int MIN_QUESTION_SCORE = 0;

    int MAX_QUESTION_SCORE = 10;

    int SUPERCLASS_QUESTION_SCORE = -1;

    int PASS_QUESTION_SCORE = 6;

    String SUPERCLASS_MESSAGE = "问题超纲";

    String ABILITY_VALUE_MESSAGE = "能力值范围为" + MIN_ABILITY_VALUE + "-" + MAX_ABILITY_VALUE;

    String QUESTION_SCORE_MESSAGE = "题目得分数值范围为" + MIN_QUESTION_SCORE + "-" + MAX_QUESTION_SCORE + "，" + SUPERCLASS_QUESTION_SCORE + "代表" + SUPERCLASS_MESSAGE;

    String QUESTION_SCORE_LOCK = "questionScoreLock:%d:%d";

    String INTERVIEW_SUMMARY_LOCK = "interviewSummaryLock:";

}
