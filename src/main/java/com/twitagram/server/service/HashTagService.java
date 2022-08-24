package com.twitagram.server.service;

import com.twitagram.server.dto.response.HashTagsResponseDto;
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

import javax.mail.search.SearchTerm;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Hashtags> tagCheck = hashtagRepository.findAllByTags(tag, pageable);
        if (tagCheck.isEmpty()) {
            return ResponseDto.fail("400", "Wrong Tag");
        }
        System.out.println("tagCheck : " + tagCheck);

//        List<Post> postList = new ArrayList<Post>();
//        for (Hashtags el : tagCheck) {
//            Post post = el.getPost();
//            postList.add(post);
//        }
//        System.out.println("postList : " + postList);
//        return ResponseDto.success(null, "200", "success");

        List<PostResponseDto> dtoList = new ArrayList<>();

        for (Hashtags hashtags : tagCheck) {
            Post post = hashtags.getPost();
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
                .currpage(tagCheck.getNumber() + 1)
                .totalpage(tagCheck.getTotalPages())
                .currcontent(tagCheck.getNumberOfElements())
                .posts(dtoList)
                .build();
        return ResponseDto.success(pageDto, "200", "Successfully get posts");


    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getHashTagsRank() {
//        List<Post> postList = new ArrayList<Post>();
//        for (Hashtags el : tagCheck) {
//            Post post = el.getPost();
//            postList.add(post);
//        }
//        System.out.println("postList : " + postList);
//        return ResponseDto.success(null, "200", "success");
        List<Hashtags> hashtags = hashtagRepository.findAll();
        List<String> hashtagsList = new ArrayList<>();
        for (Hashtags el : hashtags) {
            hashtagsList.add(el.getTags());
        }
        System.out.println("hashtags : " + hashtagsList);

        Set<String> set = new HashSet<>(hashtagsList);
        List<String> hashtagsRankList = new ArrayList<>();
        List<Integer> hashtagsRankList2 = new ArrayList<>();


        for (String str : set) {
            System.out.println(str);
            hashtagsRankList.add(str);
            System.out.println(hashtagsRankList);
            System.out.println(Collections.frequency(hashtagsList, str));
            hashtagsRankList2.add(Collections.frequency(hashtagsList, str));
            System.out.println(hashtagsRankList2);
        }
//        HashMap<String, Integer> map = new HashMap<>();
//        for (int i = 0; i < hashtagsRankList.size(); i++) {
//           map.put(hashtagsRankList.get(i),hashtagsRankList2.get(i));
//        }
//        System.out.println("----------");
//        System.out.println("map : "+map);
        List<String[]> htg = new ArrayList<String[]>();
        for (int i = 0; i<hashtagsRankList.size();i++){
            String[] tagrank = new String[2];
            tagrank[0] = hashtagsRankList.get(i);
            tagrank[1] = hashtagsRankList2.get(i)+"";
            htg.add(tagrank);
        }
        System.out.println(htg);
        HashTagsResponseDto tagsDto = HashTagsResponseDto.builder()
                .hashtags(htg)
                .build();
        return ResponseDto.success(tagsDto, "200", "Successfully get hashtag ranking");
    }

}
