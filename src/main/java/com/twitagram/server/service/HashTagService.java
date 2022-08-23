package com.twitagram.server.service;

import com.twitagram.server.dto.response.PostPageDto;
import com.twitagram.server.dto.response.PostResponseDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.*;
import com.twitagram.server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;


    @Transactional(readOnly = true)
    public ResponseDto<?> getPostByHashTag(String tag, int page, int limit, String sortBy, UserDetails userDetails) {
        Optional<Hashtags> tagCheck = hashtagRepository.findHashtagsByTags(tag);
        if (tagCheck.isEmpty()) {
            return ResponseDto.fail("400", "Wrong Tag");
        }

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> postList = postRepository.findPostsById(tagCheck.get().getPost().getId(),pageable);

        List<PostResponseDto> dtoList = new ArrayList<>();

        for (Post post : postList) {
            List<Image> imageList = imageRepository.findAllByPost_Id(post.getId());
            List<Hashtags> hashtagsList = hashtagRepository.findAllByPost_Id(post.getId());

            List<String> URLS = new ArrayList<String>();
            List<String> Tags = new ArrayList<String>();

            int LikeCount = likesRepository.countAllByPost_Id(post.getId());
            Optional<Member> member = memberRepository.findByUsername(userDetails.getUsername());
            Likes LikeCheck = likesRepository.findByMember_Id(member.get().getId());
            for (Hashtags s : hashtagsList) {
                Tags.add(s.getTags());
            }
            for (Image s : imageList) {
                URLS.add(s.getImageurls());
            }
            String time = post.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

            int followCount = followRepository.countByMember_IdAndFollow_Id(member.get().getId(), post.getMember().getId());
            dtoList.add(PostResponseDto.builder()
                            .id(post.getId())
                            .username(post.getMember().getUsername())
                            .userprofile(post.getMember().getUserprofile())
                            .content(post.getContent())
                            .imageurls(URLS)
                            .hashtags(Tags)
                            .Ismine(Objects.equals(post.getMember().getUsername(), userDetails.getUsername()))
                            .time(time)
                            .Isliked(LikeCheck != null)
                            .Isfollowing(followCount != 0)
//                            .numcomments()
                            .numlikes(LikeCount)
                            .build()
            );
        }
        PostPageDto pageDto = PostPageDto.builder()
                .currpage(postList.getNumber() + 1)
                .totalpage(postList.getTotalPages())
                .currcontent(postList.getNumberOfElements())
                .posts(dtoList)
                .build();
//        return ResponseDto.success(pageDto);
        return ResponseDto.success(pageDto, "200", "게시글 전체 조회");


    }

}
