package com.mc.common.utils.file;

import com.mc.common.utils.ServletUtils;
import com.mc.common.utils.spring.SpringUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 采用minio 存取文件工具类
 * @author mc
 * @date 2023/3/3
 */
public class MinioUtil {
    /**
     * 上传文件
     *
     * @param bucketName 桶名称
     * @param fileName
     * @throws IOException
     */
    public static String uploadFile(String bucketName, String fileName, MultipartFile multipartFile) throws IOException
    {
        String url = "";
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        try (InputStream inputStream = multipartFile.getInputStream())
        {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(inputStream, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).build());
            url = url.substring(0, url.indexOf('?'));
            System.out.println("上传文件到minio地址:"+url);
            return ServletUtils.urlDecode(url);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }
}
