package com.twitagram.server.contorller;


import com.twitagram.server.dto.request.PostRequestDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor // 생성자 주입
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/post")  // <form> 요소가 파일이나 이미지를 서버로 전송할 때 주로 사용!!  HttpServletRequest request 추가해줘야함.
    public ResponseDto<?> createPost(@ModelAttribute PostRequestDto postRequestDto) throws IOException {
        // ModelAtrribute 사용시 Request에 Setter를 추가해주거나 모든 필드를 요소를 받는 생성자를 만들면 된다.@AllArgsConstructor
        //  https://minchul-son.tistory.com/546
        postService.createPost(postRequestDto);
        return ResponseDto.success("성공했습니다.");
    }
}
