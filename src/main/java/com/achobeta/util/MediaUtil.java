package com.achobeta.util;

import cn.hutool.http.HttpResponse;
import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.zip.Adler32;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 13:19
 */
@Slf4j
public class MediaUtil {

    public final static String TEMP_RESOURCE_PATH = "./temp/"; // 临时文件夹的文件都是用完就删的，所以我就觉得这个变量没必要写在配置文件里了

    public final static String COMPRESS_FORMAT_NAME = "jpg"; // 压缩图片格式

    public final static String COMPRESS_FORMAT_SUFFIX = "." + COMPRESS_FORMAT_NAME; // 压缩图片格式

    public final static float COMPRESS_SCALE = 1.0f; // 压缩图片大小

    public final static float COMPRESS_QUALITY = 0.5f; // 压缩图片质量

    private final static Tika TIKA = new Tika();

    public static byte[] compressImage(byte[] bytes) {
        try(InputStream inputStream = getInputStream(bytes);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // 使用 thumbnailator 进行压缩，指定输出格式
            Thumbnails.of(inputStream)
                    .outputFormat(COMPRESS_FORMAT_NAME)
                    .scale(COMPRESS_SCALE)
                    .outputQuality(COMPRESS_QUALITY)
                    .toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static InputStream getInputStream(String url) throws IOException {
        HttpResponse response = HttpRequestUtil.getRequestAndExecute(url);
        return HttpRequestUtil.isAccessible(response) ? response.bodyStream() : null;
    }

    public static InputStream getInputStream(byte[] bytes) {
        return Objects.nonNull(bytes) ? new ByteArrayInputStream(bytes) : null;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        return Objects.nonNull(inputStream) ? inputStream.readAllBytes() : null;
    }

    public static byte[] getBytes(String url) {
        try (InputStream inputStream = getInputStream(url)) {
            return getBytes(inputStream);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public static byte[] getBytes(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return getBytes(inputStream);
        }  catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        }
    }

    public static String getContentType(InputStream inputStream) throws IOException {
        return TIKA.detect(inputStream);
    }

    public static String getContentType(MultipartFile file) {
        try(InputStream inputStream = file.getInputStream()) {
            return getContentType(inputStream);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static String getContentType(byte[] data) {
        try(InputStream inputStream = MediaUtil.getInputStream(data)) {
            return getContentType(inputStream);
        } catch (IOException e) {
            throw new GlobalServiceException(e.getMessage(), GlobalServiceStatusCode.RESOURCE_NOT_VALID);
        }
    }

    public static String getTempFilePath(String suffix) {
        String tempResourcePath = TEMP_RESOURCE_PATH;
        File tempDir = new File(tempResourcePath);
        if(!tempDir.exists()) {
            tempDir.mkdir();
        }
        return tempResourcePath + ResourceUtil.getSimpleFileName(suffix);
    }

    private static FileOutputStream createAndGetFileOutputStream(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        return new FileOutputStream(file);
    }

    public static String getAdler32(byte[] bytes) {
        Adler32 adler32 = new Adler32();
        adler32.update(bytes);
        return String.valueOf(adler32.getValue());
    }

    public static <T> T createTempFileGetSomething(String originalName, byte[] data, Function<File, T> converter) {
        String fileNameSuffix = ResourceUtil.getSuffix(originalName);
        // 获取即将创建的临时文件的路径
        String tempFilePath = getTempFilePath(fileNameSuffix);
        File tempFile = new File(tempFilePath);
        // 创建并写入，应用后删除
        try (FileOutputStream outputStream = createAndGetFileOutputStream(tempFile)) {
            outputStream.write(data);
            outputStream.flush();
            return converter.apply(tempFile);
        }  catch (IOException e) {
            throw new GlobalServiceException(e.getMessage());
        } finally {
            tempFile.delete();
        }
    }

}
