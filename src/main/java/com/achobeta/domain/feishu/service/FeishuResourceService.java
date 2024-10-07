package com.achobeta.domain.feishu.service;

import com.achobeta.domain.feishu.model.dto.FeishuResourceQueryDTO;
import com.achobeta.domain.feishu.model.entity.FeishuResource;
import com.achobeta.domain.feishu.model.vo.FeishuResourceQueryVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lark.oapi.service.drive.v1.model.ImportTask;

/**
* @author 马拉圈
* @description 针对表【feishu_resource(飞书资源表)】的数据库操作Service
* @createDate 2024-09-28 15:11:38
*/
public interface FeishuResourceService extends IService<FeishuResource> {

    FeishuResource createAndGetFeishuResource(String ticket, String originalName);

    void updateFeishuResource(Long id, ImportTask importTask);

    String redirectByTicket(String ticket);

    FeishuResourceQueryVO queryResource(FeishuResourceQueryDTO feishuResourceQueryDTO);

    String getSystemUrl(String ticket);

}
