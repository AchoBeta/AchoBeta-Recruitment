package com.achobeta.email.model.po;

import com.achobeta.util.MediaUtil;
import jakarta.activation.DataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-26
 * Time: 18:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment implements DataSource {

    private String fileName;

    private byte[] bytes;

    public static EmailAttachment of(MultipartFile multipartFile){
        return new EmailAttachment(multipartFile.getOriginalFilename(), MediaUtil.getBytes(multipartFile));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return MediaUtil.getInputStream(this.bytes);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public String getContentType() {
        return MediaUtil.getContentType(this.bytes);
    }

    @Override
    public String getName() {
        return this.fileName;
    }
}
