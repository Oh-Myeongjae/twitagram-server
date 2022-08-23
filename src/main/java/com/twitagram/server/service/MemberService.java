package com.twitagram.server.service;

import com.twitagram.server.dto.request.LoginRequestDto;
import com.twitagram.server.dto.request.MemberRequestDto;
import com.twitagram.server.dto.request.TokenDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.Member;
import com.twitagram.server.repository.MemberRepository;
import com.twitagram.server.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        //username check
        if (null != isPresentMemberByUsername(requestDto.getUsername())) {
            return ResponseDto.fail("400", "Already existing username.");
        }
        //email check
        if (null != isPresentMemberByEmail(requestDto.getEmail())) {
            return ResponseDto.fail("400", "Already existing email.");
        }
        //“Please write proper email address to email field.”
        if (requestDto.getEmail().isBlank()) {
            return ResponseDto.fail("400", "Please write proper email address to email field.");
        }
        //“Please write proper password to Password field.”
        if (requestDto.getPassword().isBlank()) {
            return ResponseDto.fail("400", "Please write proper password to Password field.");
        }

        Member member = Member.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .userprofile("https://joeschmoe.io/api/v1/" + requestDto.getUsername())
                .build();

        memberRepository.save(member);
        return ResponseDto.success(null, "200", "Successfully sign up.");
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
//    @Transactional(readOnly = true)
//    public Member isPresentMemberByPassword(String password) {
//        Optional<Member> optionalMember = memberRepository.findByPassword(password);
//        return optionalMember.orElse(null);
//    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMemberByEmail(requestDto.getEmail());
        if (null == member) {
            return ResponseDto.fail("400", "Not existing email or wrong password");
        }
//        if (null == isPresentMemberByPassword(requestDto.getPassword())){
//            return ResponseDto.fail("400", "Not existing email or wrong password");
//        }
        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("400", "Not existing email or wrong password");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(null, "200", "Successfully logged in");
    }

    public ResponseDto<?> logout(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
//            return ResponseDto.fail("403", "Token is not valid");
//        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("400",
                    "사용자를 찾을 수 없습니다.");
        }
        tokenProvider.deleteRefreshToken(member);

        return ResponseDto.success(null, "200", "Successfully logged out");
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
//        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }


}
