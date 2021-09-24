package com.example.youtubedb.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    private AmazonS3 amazonS3Client;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PostConstruct
    public void setAmazonS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        amazonS3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = getNewFileName(multipartFile.getOriginalFilename(), dirName);
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String getNewFileName(String originalFilename, String dirName) {
        Random randomGenerator = new Random();
        int randNum = randomGenerator.nextInt(100000);
        String fileName = String.valueOf(System.currentTimeMillis()) + String.valueOf(randNum) + originalFilename.substring(originalFilename.lastIndexOf("."));
        return dirName + "/" + fileName;
    }
}