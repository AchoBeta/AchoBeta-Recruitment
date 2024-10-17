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
 * 本接口大部分方法都需要真实存在一次请求才能访问
 */
public interface ResourceService {

    /**
     * 此方法可能需要真实存在一次请求才能调用（权限等级大于 FREE_ACCESS 时，此请求需要用到 HttpServletRequest，内部会尝试获取当前请求的 HttpServletRequest）
     */
    DigitalResource analyzeCode(Long code, ResourceAccessLevel level);

    DigitalResource analyzeCode(Long code);

    void preview(Long code, HttpServletResponse response);

    void download(Long code, HttpServletResponse response);

    byte[] load(Long code);

    void checkImage(Long code);

    Boolean shouldRemove(Long code, Long old);

    String getSystemUrl(Long code);

    String gerObjectUrl(Long code, Boolean hidden);

    Long upload(Long userId, String originalName, byte[] data, ResourceAccessLevel level);

    Long upload(Long userId, MultipartFile file, ResourceAccessLevel level);

    Long upload(Long userId, MultipartFile file);

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

    /**
     * 压缩图片，同时更新对象上传服务器，以及数据库的记录
     * 此方法不要求真实存在一次请求
     * @param code 资源码
     * @throws Exception 若不是图片会抛异常
     */
    void compressImage(Long code) throws Exception;

}
