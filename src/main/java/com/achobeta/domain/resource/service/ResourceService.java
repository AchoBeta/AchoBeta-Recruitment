package com.achobeta.domain.resource.service;

import com.achobeta.domain.resource.enums.ExcelTemplateEnum;
import com.achobeta.domain.resource.enums.ResourceAccessLevel;
import com.achobeta.domain.resource.model.entity.DigitalResource;
import com.achobeta.domain.resource.model.vo.OnlineResourceVO;
import com.achobeta.feishu.constants.ObjectType;
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

    DigitalResource checkAndGetResource(Long code, ResourceAccessLevel level);

    DigitalResource analyzeCode(Long code);

    void download(Long code, HttpServletResponse response);

    void preview(Long code, HttpServletResponse response);

    byte[] load(Long code);

    void checkImage(Long code);

    void checkAndRemoveImage(Long code, Long old);

    String getSystemUrl(Long code);

    String gerObjectUrl(Long code, Boolean hidden);

    Long upload(Long userId, String originalName, byte[] data, ResourceAccessLevel level);

    Long upload(Long userId, MultipartFile file, ResourceAccessLevel level);

    Long upload(Long userId, MultipartFile file);

    Long uploadImage(Long userId, MultipartFile file, ResourceAccessLevel level);

    /**
     * ⚠️由于飞书的限制，此接口会强制转扩展名⚠️
     * 错误的参数，可能会导致乱码、或者表格等文件 encrypt_file 的情况！
     * 所以务必在此方法之前检测好文件二进制类型与 objectType 适配！
     */
    OnlineResourceVO synchronousFeishuUpload(Long managerId, byte[] bytes, ResourceAccessLevel level, ObjectType objectType, String fileName, Boolean synchronous);

    OnlineResourceVO synchronousFeishuUpload(Long managerId, MultipartFile file, ResourceAccessLevel level, ObjectType objectType, String fileName, Boolean synchronous);

    <E> OnlineResourceVO uploadExcel(Long managerId, ExcelTemplateEnum excelTemplateEnum, Class<E> clazz, List<E> data, ResourceAccessLevel level, String fileName, Boolean synchronous);

    List<Long> uploadList(Long userId, List<MultipartFile> fileList);

    void setAccessLevel(Long resourceId, ResourceAccessLevel level);

    void remove(Long code);

    /**
     * 删除一个资源，宽恕删除失败的情况
     * @param code 资源码
     */
    void removeKindly(Long code);

    void checkBlockUser(Long userId);

    void blockUser(Long userId, Long blockDDL);
}
