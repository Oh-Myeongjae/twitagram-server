package com.twitagram.server.contorller;

import com.twitagram.server.dto.request.FollowDto;
import com.twitagram.server.service.FollowService;
import com.twitagram.server.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class followController {

    private final FollowService followService;

    @PostMapping("api/user/follow")
    public ResponseDto<?> userFollow(@RequestBody FollowDto followDto, @AuthenticationPrincipal UserDetails user) {
        return followService.userFollow(user.getUsername(),followDto.getUsername());
    }

    @DeleteMapping("api/user/unfollow")
    public ResponseDto<?> userUnFollow(@RequestBody FollowDto followDto, @AuthenticationPrincipal UserDetails user) {
        System.out.println("언팔로우 : "+ followDto.getUsername());
        return followService.userUnFollow(user.getUsername(),followDto.getUsername());
    }
}
