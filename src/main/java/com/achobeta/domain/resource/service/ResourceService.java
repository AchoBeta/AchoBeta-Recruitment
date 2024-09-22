package com.achobeta.domain.resource.service;

import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.DigitalResourceVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:16
 */
public interface ResourceService {

    DigitalResource checkAndGetResource(Long code);

    DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level);

    String analyzeCode(Long code);

    Long upload(Long userId, MultipartFile file);

    List<Long> uploadList(Long userId, List<MultipartFile> fileList);

    void setAccessLevel(Long id, ResourceAccessLevel level);

    void remove(Long code);

}
