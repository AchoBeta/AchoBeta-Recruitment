package com.achobeta.domain.users.model.vo;

import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.users.model.vo.UserVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:12
 */
@Data
public class MemberVO {

    private Long id;

    private Long managerId;

    private Long parentId;

    private UserVO parentManager;

    private Long resumeId;

    private StuResumeVO stuResumeVO;

}
