package com.achobeta.domain.resource.service;

import com.achobeta.common.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created With Intellij IDEA
 * User: 马拉圈
 * Date: 2024-09-21
 * Time: 12:16
 * Description: 仅仅针对本服务内部的资源
 */
public interface ResourceService {

    DigitalResource checkAndGetResource(Long code);

    DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level);

    DigitalResource analyzeCode(Long code);

    void download(Long code, HttpServletResponse response);

    void preview(Long code, HttpServletResponse response);

    byte[] load(Long code);

    String gerObjectUrl(Long code);

    Long upload(Long userId, MultipartFile file);

    List<Long> uploadList(Long userId, List<MultipartFile> fileList);

    void setAccessLevel(Long id, ResourceAccessLevel level);

    void remove(Long code);

}
