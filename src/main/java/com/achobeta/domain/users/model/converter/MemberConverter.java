package com.achobeta.domain.users.model.converter;

import com.achobeta.domain.login.model.dto.RegisterDTO;
import com.achobeta.domain.users.model.dto.MemberDTO;
import com.achobeta.domain.users.model.entity.Member;
import com.achobeta.domain.users.model.vo.MemberVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 14:19
 */
@Mapper
public interface MemberConverter {

    MemberConverter INSTANCE = Mappers.getMapper(MemberConverter.class);

    RegisterDTO memberDTOToRegisterDTO(MemberDTO memberDTO);

    MemberVO memberToMemberVO(Member member);

}
