package com.twitagram.server.contorller;

import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.HashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class HashtagController {

    private final HashTagService hashTagService;

    //    @GetMapping("/api/hashtag/{tags}")
//    public ResponseDto<?> getPostByHashTag(@PathVariable String tags,
//                                           @RequestParam("pageNum") int page,
//                                           @RequestParam(value = "pageLimit",defaultValue = "5") int limit,
//                                           @AuthenticationPrincipal UserDetails userDetails){
//        page = page-1;
//        String sortBy = "createdAt";
//        return hashTagService.getPostByHashTag(tags,page,limit,sortBy,userDetails);
//    }
    @PostMapping("/api/hashtag")
    public ResponseDto<?> getPostByHashTag(@RequestBody String tag,
                                           @RequestParam("pageNum") int page,
                                           @RequestParam(value = "pageLimit", defaultValue = "5") int limit,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        page = page - 1;
        String sortBy = "createdAt";
        System.out.println("tags :" + tag);
        return hashTagService.getPostByHashTag(tag, page, limit, sortBy, userDetails);
    }
}
