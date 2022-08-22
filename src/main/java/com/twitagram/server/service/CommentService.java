package com.twitagram.server.service;

import com.twitagram.server.dto.request.CommentRequestDto;
import com.twitagram.server.dto.response.CommentResponseDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.Comment;
import com.twitagram.server.entity.Member;
import com.twitagram.server.entity.Post;
import com.twitagram.server.repository.CommentRepository;
import com.twitagram.server.repository.PostRepository;
import com.twitagram.server.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TokenProvider tokenProvider;
    private final PostService postService;
    private final PostRepository postRepository;

//    private final HashTagRepository hashTagRepository;
//
//    private final HashTagService hashTagService;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, Integer id, HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) {
            return ResponseDto.fail("400", "AccessToken.");
        }

        Member member = validateMember(request);
        if (member == null) {
            return ResponseDto.fail("400", "Fail to create new comment.");
        }

        Post post = postService.isPresentPost(id);
        if (post == null) {
            return ResponseDto.fail("400", "Fail to create new comment.Post 없음");
        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

//        hashTagService.createHashTag(requestDto.getHashtags());

        return ResponseDto.success(null, "200", "Successfully created new comment.");
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
    @Transactional
    public ResponseDto<?> getComments(Integer id, Integer pageNum, Integer pageLimit, HttpServletRequest request){
        Post post = isPresentPost(id);
        if (post == null){
            return ResponseDto.fail("400","Fail to get comments. Wrong page number");
        }

        Pageable pageable = PageRequest.of(pageNum,pageLimit);
        List<Comment> commentList = commentRepository.findAllByPost(post,pageable);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        if(null == request.getHeader("Authorization")){
            for (Comment comments : commentList){
                commentResponseDtoList.add(
                        CommentResponseDto.builder()
                                .id(comments.getId())
                                .username(comments.getMember().getUsername())
                                .userprofile(comments.getMember().getUserprofile())
                                .content(comments.getContent())
                                .Ismine(false)
                                .build()
                );
            }
            return ResponseDto.success(commentResponseDtoList,"200","Successfully get comments.");
        }
        Member member = validateMember(request);
        for (Comment comments : commentList){
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comments.getId())
                            .username(comments.getMember().getUsername())
                            .userprofile(comments.getMember().getUserprofile())
                            .content(comments.getContent())
                            .Ismine(comments.getMember().getUsername().equals(member.getUsername())).build()
            );
        }
        return ResponseDto.success(commentResponseDtoList,"200","Successfully get comments.");
    }
    @Transactional(readOnly = true)
    public Post isPresentPost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }


//    @Transactional
//    public ResponseDto<?> updateComment(int commentId, CommentRequestDto requestDto) {
//
//    }

//    @Transactional
//    public ResponseDto<?> deleteComment(int commentId) {
//        if(commentRepository.existsById(commentId)) {
//            return ResponseDto.fail("400", "Already deleted post");
//        }
//        commentRepository.deleteById(commentId);
//        return ResponseDto.success(null, "200", "Successfully deleted comment");
//    }

}
