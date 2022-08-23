package com.twitagram.server.service;

import com.twitagram.server.dto.request.PostRequestDto;
import com.twitagram.server.dto.response.PostPageDto;
import com.twitagram.server.dto.response.PostResponseDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.*;
import com.twitagram.server.repository.*;
import com.twitagram.server.utils.aws.S3upload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;

    private final LikesRepository likesRepository;
    private final FollowRepository followRepository;

    private  final S3upload s3upload;

    @Transactional
    public void createPost(PostRequestDto postRequestDto, UserDetails user) throws IOException {

        List<MultipartFile> FileList = postRequestDto.getImagefiles();
        List<String> tags = postRequestDto.getHashtags();
        Optional<Member> author = memberRepository.findByUsername(user.getUsername());
        Post post = postRepository.save(
                Post.builder()
                        .content(postRequestDto.getContent())
                        .member(author.get())
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

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPost(int page, int size, String sortBy, UserDetails user) {
        Sort.Direction direction = Sort.Direction.DESC; // true: 오름차순 (asc) , 내림차순 DESC(최신 것이 위로온다)
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<Post> postList = postRepository.findAll(pageable);

        List<PostResponseDto> dtoList = new ArrayList<>();

        for(Post post : postList){
            List<Image> imageList = imageRepository.findAllByPost_Id(post.getId());
            List<Hashtags>  hashtagsList = hashtagRepository.findAllByPost_Id(post.getId());

            List<String> URLS = new ArrayList<String>();
            List<String> Tags = new ArrayList<String>();

            int LikeCount = likesRepository.countAllByPost_Id(post.getId());
            Optional<Member> member = memberRepository.findByUsername(user.getUsername());
            Likes LikeCheck = likesRepository.findByMember_Id(member.get().getId());
//            System.out.println("LikeCount = " + LikeCheck.getId());
            for(Hashtags s :  hashtagsList){
                Tags.add(s.getTags());
            }
            for(Image s :  imageList){
                URLS.add(s.getImageurls());
            }
           String time = post.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

            int followCount = followRepository.countByMember_IdAndFollow_Id(member.get().getId(),post.getMember().getId());
            dtoList.add( PostResponseDto.builder()
                            .id(post.getId())
                            .username(post.getMember().getUsername())
                            .userprofile(post.getMember().getUserprofile())
                            .content(post.getContent())
                            .imageurls(URLS)
                            .hashtags(Tags)
                            .Ismine(Objects.equals(post.getMember().getUsername(), user.getUsername()))
                            .time(time)
                            .Isliked(LikeCheck != null)
                            .Isfollowing(followCount != 0)
//                            .numcomments()
                            .numlikes(LikeCount)
                    .build()
            );
        }
        PostPageDto pageDto = PostPageDto.builder()
                .currpage(postList.getNumber()+1)
                .totalpage(postList.getTotalPages())
                .currcontent(postList.getNumberOfElements())
                .posts(dtoList)
                .build();
//        return ResponseDto.success(pageDto);
        return ResponseDto.success(pageDto,"200","게시글 전체 조회");
    }
}
