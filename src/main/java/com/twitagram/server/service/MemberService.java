package com.twitagram.server.service;

import com.twitagram.server.dto.request.MemberRequestDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.Member;
import com.twitagram.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto){
        //username check
        if (null != isPresentMemberByUsername(requestDto.getUsername())){
            return ResponseDto.fail("400","Already existing username.");
        }
        //email check
        if (null != isPresentMemberByEmail(requestDto.getEmail())){
            return ResponseDto.fail("400","Already existing email.");
        }
        //“Please write proper email address to email field.”
        if (requestDto.getEmail().isEmpty()){
            return ResponseDto.fail("400","Please write proper email address to email field.");
        }
        //“Please write proper password to Password field.”
        if (requestDto.getPassword().isEmpty()){
            return ResponseDto.fail("400","Please write proper password to Password field.");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .userprofile("https://joeschmoe.io/api/v1/"+requestDto.getUsername())
                .build();

        memberRepository.save(member);
        return ResponseDto.success(null,"200","Successfully sign up.");
    }

    @Transactional(readOnly = true)
    public Member isPresentMemberByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }
    @Transactional(readOnly = true)
    public Member isPresentMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

}
