package com.achobeta.domain.resumestate.model.vo;

import lombok.*;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-20
 * Time: 0:30
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationNoticeTemplate {

    private String studentId;

    private String username;

    private String password;

}
