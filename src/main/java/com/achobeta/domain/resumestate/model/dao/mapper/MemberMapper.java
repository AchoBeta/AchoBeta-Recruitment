package com.achobeta.domain.resumestate.model.dao.mapper;

import com.achobeta.domain.resumestate.model.entity.Member;
import com.achobeta.domain.resumestate.model.vo.SimpleMemberVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 马拉圈
* @description 针对表【member(正式成员表)】的数据库操作Mapper
* @createDate 2024-08-17 23:55:10
* @Entity com.achobeta.domain.resume.model.entity.Member
*/
public interface MemberMapper extends BaseMapper<Member> {

    List<SimpleMemberVO> queryMemberList(@Param("batchId") Long batchId);

}




