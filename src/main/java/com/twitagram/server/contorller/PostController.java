package com.twitagram.server.contorller;


import com.twitagram.server.dto.request.PostRequestDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.PostService;
import com.twitagram.server.utils.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@RequiredArgsConstructor // 생성자 주입
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")  // <form> 요소가 파일이나 이미지를 서버로 전송할 때 주로 사용!!  HttpServletRequest request 추가해줘야함.
    public ResponseDto<?> createPost(@ModelAttribute PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetails user) throws IOException {
        // ModelAtrribute 사용시 Request에 Setter를 추가해주거나 모든 필드를 요소를 받는 생성자를 만들면 된다.@AllArgsConstructor
        //  https://minchul-son.tistory.com/546
        if (postRequestDto.getContent().equals("")) {
            return ResponseDto.fail("400", "Fail to create new post.");
        }
        postService.createPost(postRequestDto, user);
        return ResponseDto.success(null, "200", "Successfully created new post.");
    }

    @GetMapping("posts") // 게시판 전체 조회
    public ResponseDto<?> getAllPosts(@RequestParam("pageNum") int page, @RequestParam("pageLimit") int limit, @AuthenticationPrincipal UserDetails user) {
        page = page - 1;  //첫번째 페이지 클라이언트에서는 1 , 서버 0
        int size = limit; //  한 페이지 당 12개
        String sortBy = "createdAt";  // 정렬항목인데 필요없을듯...
        return postService.getAllPost(page, size, sortBy, user);
    }

    @PutMapping("/post/{postId}") // 게시글 수정 (추후에 시간이 된다면 패치로 수정해보는 것도 나쁘진 않을듯!!)
    public ResponseDto<?> updatePost(@PathVariable int postId,
                                     @RequestBody PostRequestDto postRequestDto,
                                     @AuthenticationPrincipal UserDetails user) throws IOException {
        return postService.updatePost(postId, postRequestDto, user);
    }

    @PostMapping("like/{postId}")
    public ResponseDto<?> likePost(@PathVariable int postId, @AuthenticationPrincipal UserDetails user){
        return postService.likePost(postId, user);
    }

    @PostMapping("unlike/{postId}")
    public ResponseDto<?> unlikePost(@PathVariable int postId){
        return postService.unlikePost(postId);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseDto<?> deletePost(@PathVariable int postId,  @AuthenticationPrincipal UserDetails user) throws UnsupportedEncodingException {
        return postService.deletePost(postId,user);
    }
    //게시글 단일 조회
    @GetMapping("/post/{id}")
    public ResponseDto<?> getPost(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails){
        return postService.getPost(id,userDetails);
    }
}
