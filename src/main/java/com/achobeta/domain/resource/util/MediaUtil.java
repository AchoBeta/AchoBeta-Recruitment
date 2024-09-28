package com.achobeta.domain.resource.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.constants.ResourceConstants;
import com.achobeta.exception.GlobalServiceException;
import com.achobeta.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-05
 * Time: 13:19
 */
@Slf4j
public class MediaUtil {

    private final static Tika TIKA = new Tika();

    @Nullable
    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = HttpRequestUtil.openConnection(url);
        return HttpRequestUtil.isAccessible(connection) ? connection.getInputStream() : null;
    }

    @Nullable
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

    private static FileOutputStream createAndGetFileOutputStream(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        return new FileOutputStream(file);
    }

    public static <T> T createTempFileGetSomething(String originalName, byte[] data, Function<File, T> converter) {
        String tempResourcePath = ResourceConstants.TEMP_RESOURCE_PATH;
        File tempDir = new File(tempResourcePath);
        if(!tempDir.exists()) {
            tempDir.mkdir();
        }
        String fileNameSuffix = ResourceUtil.getFileNameSuffix(originalName);
        String tempFileName = ResourceUtil.getSimpleFileName(fileNameSuffix);
        File tempFile = new File(tempResourcePath + tempFileName);
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
