package com.achobeta.domain.shortlink.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.shortlink.model.dao.po.ShortLink;
import com.achobeta.domain.shortlink.model.dto.ShortLinkQueryDTO;
import com.achobeta.domain.shortlink.model.vo.ShortLinkQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-23
 * Time: 22:28
 */
@Mapper
public interface ShortLinkConverter {

    ShortLinkConverter INSTANCE = Mappers.getMapper(ShortLinkConverter.class);

    BasePageQuery shortLinkQueryDTOToBasePageQuery(ShortLinkQueryDTO shortLinkQueryDTO);

    ShortLinkQueryVO basePageResultToShortLinkQueryVO(BasePageResult<ShortLink> basePageResult);

}
