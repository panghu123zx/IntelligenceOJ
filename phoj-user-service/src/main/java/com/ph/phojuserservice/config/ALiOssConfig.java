package com.ph.phojuserservice.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@Data
@ConfigurationProperties(prefix = "ali.oss")
public class ALiOssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 阿里云oss上传图片
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadUserAvatar(MultipartFile file) throws IOException {
        //获取上传文件的输入流
        InputStream inputStream = file.getInputStream();
        //避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID()+ originalFilename.substring(originalFilename.lastIndexOf("."));

        //文件名
        fileName="image/"+fileName;
        //上传到oss
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName,fileName,inputStream);
        //文件的访问路径
        String url="https://"+ bucketName +"."+endpoint+"/"+fileName;
        //关闭oss
        ossClient.shutdown();
        System.out.println(url);
        //返回文件所在的路径
        return url;

    }

}
