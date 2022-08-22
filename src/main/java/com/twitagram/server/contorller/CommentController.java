package com.twitagram.server.contorller;

import com.twitagram.server.dto.request.CommentRequestDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

//    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.POST)
    @PostMapping("/api/comment/{id}")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        @PathVariable int id, HttpServletRequest request){
        return commentService.createComment(requestDto, id, request);
    }

    @GetMapping("/api/comment/{id}")
    public ResponseDto<?> getComments(@PathVariable int id, @RequestParam("pageNum") Integer pageNum,
                                      @RequestParam(value = "pageLimit",defaultValue = "5") Integer pageLimit,
                                      HttpServletRequest request){
        return commentService.getComments(id, pageNum, pageLimit, request);
    }

//    @RequestMapping(value = "/api/comments/", method = RequestMethod.GET)
//    public ResponseDto<?> getComments(@RequestParam int postId, @RequestParam int pageNum, @RequestParam int pageLimit) {
//        return commentService.getComments(postId, pageNum, pageLimit);
//    }
//
//    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.PUT)
//    public ResponseDto<?> updateComment(@PathVariable int id, @RequestBody CommentRequestDto requestDto) {
//        return commentService.updateComment(id, requestDto);
//    }
//
//    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.DELETE)
//    public ResponseDto<?> deleteComment(@PathVariable int id) {
//        return commentService.deleteComment(id);
//    }

}
