package com.twitagram.server.service;

import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.Follow;
import com.twitagram.server.entity.Member;
import com.twitagram.server.entity.Post;
import com.twitagram.server.repository.FollowRepository;
import com.twitagram.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    public ResponseDto<?> userFollow(String username, String target) {
        Optional<Member> user = memberRepository.findByUsername(username);
        Optional<Member> following = memberRepository.findByUsername(target);
        followRepository.save(
                Follow.builder()
                .member(user.get())
                .follow(following.get())
                .build()
        );
        return ResponseDto.success(null,"200","Follow "+target);
    }

    public ResponseDto<?> userUnFollow(String username, String target) {
        Optional<Member> user = memberRepository.findByUsername(username);
        Optional<Member> following = memberRepository.findByUsername(target);
        Follow follow = followRepository.findByMember_IdAndFollow_Id(user.get().getId(),following.get().getId());
        followRepository.delete(follow);
        return ResponseDto.success(null,"200","Unfollow "+target);
    }
}
