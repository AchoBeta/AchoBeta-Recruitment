package com.achobeta.domain.resumestate.service.impl;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.login.service.LoginService;
import com.achobeta.domain.resumestate.model.converter.MemberConverter;
import com.achobeta.domain.resumestate.model.dto.MemberDTO;
import com.achobeta.domain.resumestate.model.vo.MemberVO;
import com.achobeta.domain.resumestate.model.vo.SimpleMemberVO;
import com.achobeta.domain.student.model.dto.QueryResumeDTO;
import com.achobeta.domain.student.model.vo.StuResumeVO;
import com.achobeta.domain.student.service.StuResumeService;
import com.achobeta.domain.users.model.converter.UserConverter;
import com.achobeta.domain.users.model.vo.UserVO;
import com.achobeta.domain.users.service.UserService;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.redis.RedisLock;
import com.achobeta.redis.strategy.SimpleLockStrategy;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.achobeta.domain.resumestate.model.entity.Member;
import com.achobeta.domain.resumestate.service.MemberService;
import com.achobeta.domain.resumestate.model.dao.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 马拉圈
* @description 针对表【member(正式成员表)】的数据库操作Service实现
* @createDate 2024-08-17 23:55:10
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
    implements MemberService{

    private final static String MEMBER_RESUME_LOCK = "memberResumeLock:";

    private final RedisLock redisLock;

    private final SimpleLockStrategy simpleLockStrategy;

    private final MemberMapper memberMapper;

    private final LoginService loginService;

    private final UserService userService;

    private final StuResumeService stuResumeService;

    @Override
    public Optional<Member> getMemberByResumeId(Long resumeId) {
        return this.lambdaQuery()
                .eq(Member::getResumeId, resumeId)
                .oneOpt();
    }

    @Override
    public Optional<Member> getMemberByManagerId(Long managerId) {
        return this.lambdaQuery()
                .eq(Member::getManagerId, managerId)
                .oneOpt();
    }

    @Override
    public MemberVO getMemberDetailByManagerId(Long managerId) {
        return getMemberByManagerId(managerId)
                .map(MemberConverter.INSTANCE::memberToMemberVO)
                .map(member -> {
                    StuResumeVO resumeInfo = stuResumeService.getResumeInfo(new QueryResumeDTO() {{
                        this.setResumeId(member.getResumeId());
                    }});
                    member.setStuResumeVO(resumeInfo);
                    UserVO parentManager = userService.getUserById(member.getParentId())
                            .map(UserConverter.INSTANCE::userToUserVO)
                            .orElse(null);
                    member.setParentManager(parentManager);
                    return member;
                }).orElse(null);
    }

    @Override
    public List<SimpleMemberVO> queryMemberList(Long batchId) {
        return memberMapper.queryMemberList(batchId);
    }

    @Override
    @Transactional
    public Member createMember(Long resumeId, Long parentId, MemberDTO memberDTO) {
        return redisLock.tryLockGetSomething(MEMBER_RESUME_LOCK + resumeId, () -> {
            getMemberByResumeId(resumeId).ifPresent(member -> {
                throw new GlobalServiceException(GlobalServiceStatusCode.USER_RESUME_CONFIRMED);
            });
            // 注册一个用户
            RegisterDTO registerDTO = MemberConverter.INSTANCE.memberDTOToRegisterDTO(memberDTO);
            UserEntity newManager = loginService.register(registerDTO);
            Long managerId = newManager.getId();
            log.warn("管理员 {} 注册了一个新管理员 initial username: {} initial password: {}", parentId, registerDTO.getUsername(), registerDTO.getPassword());
            // 普通用户省级为管理员
            userService.confirmByManagerId(managerId);
            // 构造对象
            Member member = new Member();
            member.setResumeId(resumeId);
            member.setManagerId(managerId);
            member.setParentId(parentId);
            // 创建
            this.save(member);
            return member;
        }, () -> null, simpleLockStrategy);
    }
}




