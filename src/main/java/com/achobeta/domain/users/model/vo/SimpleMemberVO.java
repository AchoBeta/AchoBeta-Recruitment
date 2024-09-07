package com.achobeta.domain.users.model.vo;

import com.achobeta.domain.student.model.vo.SimpleStudentVO;
import com.achobeta.domain.users.model.vo.UserVO;
import lombok.Data;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 17:58
 */
@Data
public class SimpleMemberVO {

    private Long id;

    private Long parentId;

    private Long managerId;

    private UserVO userVO;

    private SimpleStudentVO simpleStudentVO;

}
