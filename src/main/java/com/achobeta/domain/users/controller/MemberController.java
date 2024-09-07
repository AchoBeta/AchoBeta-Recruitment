package com.achobeta.domain.users.controller;

import com.achobeta.common.SystemJsonResponse;
import com.achobeta.common.annotation.Intercept;
import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.users.model.vo.MemberVO;
import com.achobeta.domain.users.model.vo.SimpleMemberVO;
import com.achobeta.domain.users.service.MemberService;
import com.achobeta.domain.users.context.BaseContext;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:08
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Intercept(permit = {UserTypeEnum.ADMIN})
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public SystemJsonResponse getMember() {
        Long managerId = BaseContext.getCurrentUser().getUserId();
        MemberVO memberVO = memberService.getMemberDetailByManagerId(managerId);
        return SystemJsonResponse.SYSTEM_SUCCESS(memberVO);
    }

    @GetMapping("/info/{managerId}")
    public SystemJsonResponse getMember(@PathVariable("managerId") @NotNull Long managerId) {
        MemberVO memberVO = memberService.getMemberDetailByManagerId(managerId);
        return SystemJsonResponse.SYSTEM_SUCCESS(memberVO);
    }

    @GetMapping("/list")
    public SystemJsonResponse queryMemberList(@RequestParam(name = "batchId", required = false) Long batchId) {
        List<SimpleMemberVO> simpleMemberVOList = memberService.queryMemberList(batchId);
        return SystemJsonResponse.SYSTEM_SUCCESS(simpleMemberVOList);
    }

}
