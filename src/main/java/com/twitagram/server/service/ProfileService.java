package com.twitagram.server.service;

import com.twitagram.server.dto.response.*;
import com.twitagram.server.entity.*;
import com.twitagram.server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final FollowRepository followRepository;
    private  final CommentRepository commentRepository;

    public ResponseDto<?> getAllPost(int page, int size, String sortBy, String username, boolean mypage) {
        Sort.Direction direction = Sort.Direction.DESC; // true: 오름차순 (asc) , 내림차순 DESC(최신 것이 위로온다)
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Optional<Member> member = memberRepository.findByUsername(username);
        Page<Post> postList = postRepository.findAllByMember(member.get(),pageable);
        List<PostResponseDto> dtoList = new ArrayList<>();

        for(Post post : postList){
            List<Image> imageList = imageRepository.findAllByPost_Id(post.getId());
            List<Hashtags>  hashtagsList = hashtagRepository.findAllByPost_Id(post.getId());

            List<String> URLS = new ArrayList<String>();
            List<String> Tags = new ArrayList<String>();

            for(Hashtags s :  hashtagsList){
                Tags.add(s.getTags());
            }
            for(Image s :  imageList){
                URLS.add(s.getImageurls());
            }
            String time = post.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int LikeCheck = likesRepository.countByMember_IdAndPost_Id(member.get().getId(),post.getId());
            int followCount = followRepository.countByMember_IdAndFollow_Id(member.get().getId(),post.getMember().getId());
            int  numComment = commentRepository.countByPost_Id(post.getId());
            int LikeCount = likesRepository.countAllByPost_Id(post.getId());

            dtoList.add( PostResponseDto.builder()
                    .id(post.getId())
                    .username(post.getMember().getUsername())
                    .userprofile(post.getMember().getUserprofile())
                    .content(post.getContent())
                    .imageurls(URLS)
                    .hashtags(Tags)
                    .Ismine(mypage)
                    .time(time)
                    .Isliked(LikeCheck != 0)
                    .Isfollowing(followCount != 0)
                    .numcomments(numComment)
                    .numlikes(LikeCount)
                    .build()
            );
        }
        ProfilePageDto pageDto = ProfilePageDto.builder()
                .currpage(postList.getNumber()+1)
                .totalpage(postList.getTotalPages())
                .currcontent(postList.getNumberOfElements())
                .totalelements(postList.getTotalElements())
                .isme(mypage)
                .posts(dtoList)
                .build();
//        return ResponseDto.success(pageDto);
        return ResponseDto.success(pageDto,"200","Successfully get posts");
    }

    public ResponseDto<?> getUserInfo(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        int member_id = member.get().getId();
        int postCount = postRepository.countByMember_Id(member_id);
        int following = followRepository.countByMember_Id(member_id);
        int follower = followRepository.countByFollow_Id(member_id);
        ProfileInfoDto profileInfoDto = ProfileInfoDto.builder()
                .username(member.get().getUsername())
                .userprofile(member.get().getUserprofile())
                .numposts(postCount)
                .numfollowing(following)
                .numfollowers(follower)
                .build();
        return ResponseDto.success(profileInfoDto,"200","Successfully get profile info.");
    }

    public ResponseDto<?> getAllfollowing(int page, int size, String sortBy, String username, boolean mypage) {
        Sort.Direction direction = Sort.Direction.DESC; // true: 오름차순 (asc) , 내림차순 DESC(최신 것이 위로온다)
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Optional<Member> member = memberRepository.findByUsername(username);
//        Optional<Member> following = memberRepository.findByUsername(user.getUsername());

        Page<Follow> followingList = followRepository.findAllByMember(member.get(),pageable);
        List<ProfileInfoDto> dtoList = new ArrayList<>();
        for(Follow follow : followingList){
            int member_id = follow.getFollow().getId();
            int postCount = postRepository.countByMember_Id(member_id);
            int following = followRepository.countByMember_Id(member_id);
            int follower = followRepository.countByFollow_Id(member_id);

            dtoList.add(ProfileInfoDto.builder()
                            .username(follow.getFollow().getUsername())
                            .userprofile(follow.getFollow().getUserprofile())
                            .numfollowing(following)
                            .numfollowers(follower)
                            .numposts(postCount)
                    .build());
        }

        ProfilePageDto pageDto = ProfilePageDto.builder()
                .currpage(followingList.getNumber()+1)
                .totalpage(followingList.getTotalPages())
                .currcontent(followingList.getNumberOfElements())
                .totalelements(followingList.getTotalElements())
                .isme(mypage)
                .following(dtoList)
                .build();

        return ResponseDto.success(pageDto,"200","Successfully get following list.");
    }

    public ResponseDto<?> getAllFollowers(int page, int size, String sortBy, String username, boolean mypage) {
        Sort.Direction direction = Sort.Direction.DESC; // true: 오름차순 (asc) , 내림차순 DESC(최신 것이 위로온다)
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size,sort);
        Optional<Member> member = memberRepository.findByUsername(username);

        Page<Follow> followingList = followRepository.findAllByFollow(member.get(),pageable);
        List<ProfileInfoDto> dtoList = new ArrayList<>();
        for(Follow follow : followingList){
            int member_id = follow.getMember().getId();
            int postCount = postRepository.countByMember_Id(member_id);
            int following = followRepository.countByMember_Id(member_id);
            int follower = followRepository.countByFollow_Id(member_id);

            dtoList.add(ProfileInfoDto.builder()
                    .username(follow.getMember().getUsername())
                    .userprofile(follow.getMember().getUserprofile())
                    .numfollowing(following)
                    .numfollowers(follower)
                    .numposts(postCount)
                    .build());
        }

        ProfilePageDto pageDto = ProfilePageDto.builder()
                .currpage(followingList.getNumber()+1)
                .totalpage(followingList.getTotalPages())
                .currcontent(followingList.getNumberOfElements())
                .totalelements(followingList.getTotalElements())
                .isme(mypage)
                .followers(dtoList)
                .build();

        return ResponseDto.success(pageDto,"200","Successfully get follower list");

    }
}
