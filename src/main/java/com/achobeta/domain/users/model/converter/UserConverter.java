package com.achobeta.domain.users.model.converter;

import com.achobeta.common.enums.UserTypeEnum;
import com.achobeta.domain.login.model.dao.UserEntity;
import com.achobeta.domain.users.model.dto.UserDTO;
import com.achobeta.domain.users.model.vo.UserTypeVO;
import com.achobeta.domain.users.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-08-18
 * Time: 16:37
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserVO userToUserVO(UserEntity user);

    UserEntity userDTOToUser(UserDTO userDTO);

    List<UserTypeVO> userTypeEnumListToUserTypeVOList(List<UserTypeEnum> userTypeEnumList);

}
