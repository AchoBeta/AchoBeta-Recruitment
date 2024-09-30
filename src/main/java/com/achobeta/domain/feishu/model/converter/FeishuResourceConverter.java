package com.achobeta.domain.feishu.model.converter;

import com.achobeta.common.base.BasePageQuery;
import com.achobeta.common.base.BasePageResult;
import com.achobeta.domain.feishu.model.dto.FeishuResourceQueryDTO;
import com.achobeta.domain.feishu.model.entity.FeishuResource;
import com.achobeta.domain.feishu.model.vo.FeishuResourceQueryVO;
import com.lark.oapi.service.drive.v1.model.ImportTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-28
 * Time: 15:36
 */
@Mapper
public interface FeishuResourceConverter {

    FeishuResourceConverter INSTANCE = Mappers.getMapper(FeishuResourceConverter.class);

    FeishuResource importTaskToFeishuResource(ImportTask importTask);

    BasePageQuery feishuResourceQueryDTOToBasePageQuery(FeishuResourceQueryDTO feishuResourceQueryDTO);

    FeishuResourceQueryVO basePageResultToFeishuResourceQueryVO(BasePageResult<FeishuResource> basePageResult);

}
