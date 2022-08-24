package com.twitagram.server.contorller;

import com.twitagram.server.dto.request.CommentRequestDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    //    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.POST)
//    @PostMapping("/api/comment/{id}")
//    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
//                                        @PathVariable int id, HttpServletRequest request) {
//        return commentService.createComment(requestDto, id, request);
//    }

    @PostMapping("/api/comment/{id}")
    public ResponseDto<?> createComment(@PathVariable int id,
                                        @RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.createComment(id,requestDto,userDetails);
    }

//    @GetMapping("/api/comments/{id}")
//    public ResponseDto<?> getComments(@PathVariable int id, @RequestParam("pageNum") Integer pageNum,
//                                      @RequestParam(value = "pageLimit", defaultValue = "5") Integer pageLimit,
//                                      HttpServletRequest request) {
//        return commentService.getComments(id, pageNum, pageLimit, request);
//    }

    @GetMapping("/api/comments/{id}")
    public ResponseDto<?> getComments(@PathVariable int id, @RequestParam("pageNum") int page,
                                      @RequestParam(value = "pageLimit",defaultValue = "5") int limit,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        page = page -1;
        String sortBy = "createdAt";
        return commentService.getComments(id, page, limit, sortBy, userDetails);
    }

//    @PutMapping(value = "/api/comment/{id}")
//    public ResponseDto<?> updateComment(@PathVariable int id,
//                                        @RequestBody CommentRequestDto requestDto,
//                                        HttpServletRequest request) {
//        return commentService.updateComment(id, requestDto, request);
//    }

    @PutMapping(value = "/api/comment/{id}")
    public ResponseDto<?> updateComment(@PathVariable int id, @RequestBody CommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.updateComment(id,requestDto,userDetails);
    }

    @DeleteMapping(value = "/api/comment/{id}")
    public ResponseDto<?> deleteComment(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails){
        return commentService.deleteComment(id,userDetails);
    }

}
