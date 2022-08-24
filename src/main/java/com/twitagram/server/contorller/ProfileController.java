package com.twitagram.server.contorller;

import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/api/profile/posts") // 게시판 전체 조회
    public ResponseDto<?> profilePosts(@RequestParam("pageNum") int page,
                                       @RequestParam("pageLimit") int limit,
                                       HttpServletRequest request,
                                       @AuthenticationPrincipal UserDetails user) {
        page = page - 1;  //첫번째 페이지 클라이언트에서는 1 , 서버 0
        int size = limit; //  한 페이지 당 12개
        String sortBy = "createdAt";  // 정렬항목인데 필요없을듯...
        String username = request.getParameter("username");
        boolean mypage = false;
        if(username.equals("")){
            username+=user.getUsername();
            mypage = true;
        }
        return profileService.getAllPost(page, size, sortBy, username, mypage);
    }

    @GetMapping("/api/profile/following") // 게시판 전체 조회
    public ResponseDto<?> profilefollowing(@RequestParam("pageNum") int page,
                                       @RequestParam("pageLimit") int limit,
                                       HttpServletRequest request,
                                       @AuthenticationPrincipal UserDetails user) {
        page = page - 1;  //첫번째 페이지 클라이언트에서는 1 , 서버 0
        int size = limit; //  한 페이지 당 12개
        String sortBy = "createdAt";  // 정렬항목인데 필요없을듯...
        String username = request.getParameter("username");
        boolean mypage = false;
        if(username.equals("")){
            username+=user.getUsername();
            mypage = true;
        }
        return profileService.getAllfollowing(page, size, sortBy, username, mypage);
    }

    @GetMapping("/api/profile/followers") // 게시판 전체 조회
    public ResponseDto<?> profileFollowers(@RequestParam("pageNum") int page,
                                           @RequestParam("pageLimit") int limit,
                                           HttpServletRequest request,
                                           @AuthenticationPrincipal UserDetails user) {
        page = page - 1;  //첫번째 페이지 클라이언트에서는 1 , 서버 0
        int size = limit; //  한 페이지 당 12개
        String sortBy = "createdAt";  // 정렬항목인데 필요없을듯...
        String username = request.getParameter("username");
        boolean mypage = false;
        if(username.equals("")){
            username+=user.getUsername();
            mypage = true;
        }
        return profileService.getAllFollowers(page, size, sortBy, username, mypage);
    }


    @GetMapping("api/profile/info/{username}")
    public ResponseDto<?> profileinfo(@PathVariable String username, HttpServletRequest request, @AuthenticationPrincipal UserDetails user) {
        if(username.equals(""))username = user.getUsername();
        return profileService.getUserInfo(username);
    }

    @GetMapping("api/profile/info")
    public ResponseDto<?> profileinfo(HttpServletRequest request, @AuthenticationPrincipal UserDetails user) {
        return profileService.getUserInfo(user.getUsername());
    }
}
