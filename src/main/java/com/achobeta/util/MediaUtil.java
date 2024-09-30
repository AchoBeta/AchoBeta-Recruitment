package com.achobeta.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.domain.resource.util.ResourceUtil;
import com.achobeta.exception.GlobalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
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

    private final static Tika TIKA = new Tika();

    public static InputStream getInputStream(String url) throws IOException {
        HttpURLConnection connection = HttpRequestUtil.openConnection(url);
        return HttpRequestUtil.isAccessible(connection) ? connection.getInputStream() : null;
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
        String fileNameSuffix = ResourceUtil.getFileNameSuffix(originalName);
        String tempFilePath = getTempFilePath(fileNameSuffix);
        File tempFile = new File(tempFilePath);
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
