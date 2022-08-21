package com.twitagram.server.service;

import com.twitagram.server.dto.request.PostRequestDto;
import com.twitagram.server.entity.Hashtags;
import com.twitagram.server.entity.Image;
import com.twitagram.server.entity.Post;
import com.twitagram.server.repository.HashtagRepository;
import com.twitagram.server.repository.ImageRepository;
import com.twitagram.server.repository.PostRepository;
import com.twitagram.server.utils.aws.S3upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    private  final S3upload s3upload;

    @Transactional
    public void createPost(PostRequestDto postRequestDto) throws IOException {

        List<MultipartFile> FileList = postRequestDto.getImagefiles();
        List<String> tags = postRequestDto.getHashtags();

        Post post = postRepository.save(
                Post.builder()
                        .content(postRequestDto.getContent())
                        .build()
        );

        if(FileList != null){
            for(MultipartFile file : FileList){
                String url = s3upload.Uploader(file);
                imageRepository.save(
                        Image.builder()
                                .imageurls(url)
                                .post(post)
                                .build()
                );
            }
        }

        if(tags != null){
            for(String tag : tags){
                hashtagRepository.save(
                        Hashtags.builder()
                            .tags(tag)
                            .post(post)
                            .build()
                );
            }
        }
    }
}
