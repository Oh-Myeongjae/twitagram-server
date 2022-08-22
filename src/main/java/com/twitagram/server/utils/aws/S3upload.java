package com.twitagram.server.utils.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

@RequiredArgsConstructor
@Service
public class S3upload {

    @Value("${cloud.aws.s3.bucket}")  // 내 S3 버켓 이름!!
    private String bucketName;
    private final AmazonS3Client amazonS3Client;
    public String Uploader(MultipartFile file) throws IOException {

        String fileName = CommonUtils.buildFileName(file.getOriginalFilename()); // 파일이름
        long size = file.getSize(); // 파일 크기

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());  // 이게 머지? : 파일타입
        objectMetadata.setContentLength(size); //파일크기
        InputStream inputStream = file.getInputStream();   // 실제 데이터를 넣어준다
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));  // S3 저장 및 권한설정
        String imageUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
        imageUrl = URLDecoder.decode(imageUrl,"UTF-8");
        return imageUrl; // URL 대입!, URL 변환시 한글깨짐
    }

}
