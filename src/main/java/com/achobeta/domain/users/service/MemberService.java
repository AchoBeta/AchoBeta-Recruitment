package com.achobeta.domain.users.service;

import com.achobeta.domain.users.model.dto.MemberDTO;
import com.achobeta.domain.users.model.entity.Member;
import com.achobeta.domain.users.model.vo.MemberVO;
import com.achobeta.domain.users.model.vo.SimpleMemberVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【member(正式成员表)】的数据库操作Service
* @createDate 2024-08-17 23:55:10
*/
public interface MemberService extends IService<Member> {

    Optional<Member> getMemberByResumeId(Long resumeId);

    Optional<Member> getMemberByManagerId(Long managerId);

    MemberVO getMemberDetailByManagerId(Long managerId);

    List<SimpleMemberVO> queryMemberList(Long batchId);

    Member createMember(Long resumeId, Long parentId, MemberDTO memberDTO);

}
