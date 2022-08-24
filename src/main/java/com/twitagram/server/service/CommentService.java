package com.twitagram.server.service;

import com.twitagram.server.dto.request.CommentRequestDto;
import com.twitagram.server.dto.response.CommentPageDto;
import com.twitagram.server.dto.response.CommentResponseDto;
import com.twitagram.server.dto.response.ResponseDto;
import com.twitagram.server.entity.Comment;
import com.twitagram.server.entity.Member;
import com.twitagram.server.entity.Post;
import com.twitagram.server.repository.CommentRepository;
import com.twitagram.server.repository.MemberRepository;
import com.twitagram.server.repository.PostRepository;
import com.twitagram.server.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final MemberRepository memberRepository;

//    private final HashTagRepository hashTagRepository;
//
//    private final HashTagService hashTagService;

//    @Transactional
//    public ResponseDto<?> createComment(CommentRequestDto requestDto, Integer id, HttpServletRequest request) {
//        if (request.getHeader("Authorization") == null) {
//            return ResponseDto.fail("400", "AccessToken.");
//        }
//
//        Member member = validateMember(request);
//        if (member == null) {
//            return ResponseDto.fail("400", "Fail to create new comment.");
//        }
//
//        Post post = postService.isPresentPost(id);
//        if (post == null) {
//            return ResponseDto.fail("400", "Fail to create new comment.Post 없음");
//        }
//
//        Comment comment = Comment.builder()
//                .member(member)
//                .post(post)
//                .content(requestDto.getContent())
//                .build();
//        commentRepository.save(comment);
//
////        hashTagService.createHashTag(requestDto.getHashtags());
//
//        return ResponseDto.success(null, "200", "Successfully created new comment.");
//    }

    @Transactional
    public ResponseDto<?> createComment(int id, CommentRequestDto requestDto, UserDetails userDetails) {
        System.out.println("Username" + userDetails.getUsername());
        Optional<Member> memberCheck = memberRepository.findByUsername(userDetails.getUsername());
        if (memberCheck.isEmpty()) {
            return ResponseDto.fail("400", "Fail to create new comment.");
        }

        Post post = postService.isPresentPost(id);
        if (post == null) {
            return ResponseDto.fail("400", "Fail to create new comment.");
        }

        Comment comment = Comment.builder()
                .member(memberCheck.get())
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

//        hashTagService.createHashTag(requestDto.getHashtags());

        return ResponseDto.success(null, "200", "Successfully created new comment.");
    }

//    @Transactional
//    public Member validateMember(HttpServletRequest request) {
//        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
//            return null;
//        }
//        return tokenProvider.getMemberFromAuthentication();
//    }

    //    @Transactional
//    public ResponseDto<?> getComments(int id, Integer pageNum, Integer pageLimit, HttpServletRequest request) {
//        Post post = isPresentPost(id);
//        if (post == null) {
//            return ResponseDto.fail("400", "Fail to get comments. Wrong page number");
//        }
//
//        Pageable pageable = PageRequest.of(pageNum, pageLimit);
//        List<Comment> commentList = commentRepository.findAllByPost(post, pageable);
//        List<CommentResponseDto> comments = new ArrayList<>();
//
//        if (null == request.getHeader("Authorization")) {
//            for (Comment comment : commentList) {
//                comments.add(
//                        CommentResponseDto.builder()
//                                .id(comment.getId())
//                                .username(comment.getMember().getUsername())
//                                .userprofile(comment.getMember().getUserprofile())
//                                .content(comment.getContent())
//                                .Ismine(false)
//                                .build()
//                );
//            }
//            return ResponseDto.success(comments, "200", "Successfully get comments.");
//        }
//        Member member = validateMember(request);
//        for (Comment comment : commentList) {
//            comments.add(
//                    CommentResponseDto.builder()
//                            .id(comment.getId())
//                            .username(comment.getMember().getUsername())
//                            .userprofile(comment.getMember().getUserprofile())
//                            .content(comment.getContent())
//                            .Ismine(comment.getMember().getUsername().equals(member.getUsername()))
//                            .build()
//            );
//        }
//        return ResponseDto.success(comments, "200", "Successfully get comments.");
//    }
    @Transactional
    public ResponseDto<?> getComments(int id, Integer pageNum, Integer pageLimit, String sortBy, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = isPresentPost(id);
        if (post == null) {
            return ResponseDto.fail("400", "Fail to get comments. Wrong page number");
        }

        Sort.Direction direction = Sort.Direction.DESC; // true: 오름차순 (asc) , 내림차순 DESC(최신 것이 위로온다)
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNum, pageLimit,sort);
        Page<Comment> commentList = commentRepository.findAllByPost(post, pageable);
        List<CommentResponseDto> comments = new ArrayList<>();

        Optional<Member> memberCheck = memberRepository.findByUsername(userDetails.getUsername());
        if (memberCheck.isEmpty()) {
            for (Comment comment : commentList) {
                comments.add(
                        CommentResponseDto.builder()
                                .id(comment.getId())
                                .username(comment.getMember().getUsername())
                                .userprofile(comment.getMember().getUserprofile())
                                .content(comment.getContent())
                                .Ismine(false)
                                .build()
                );
            }
            return ResponseDto.success(comments, "200", "Successfully get comments.");
        }
        for (Comment comment : commentList) {
            comments.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .username(comment.getMember().getUsername())
                            .userprofile(comment.getMember().getUserprofile())
                            .content(comment.getContent())
                            .Ismine(comment.getMember().getUsername().equals(memberCheck.get().getUsername()))
                            .build()
            );
        }
        CommentPageDto commentPageDto = CommentPageDto.builder()
                .currpage(commentList.getNumber()+1)
                .totalpage(commentList.getTotalPages())
                .currcontent(commentList.getNumberOfElements())
                .comments(comments)
                .build();
        return ResponseDto.success(commentPageDto, "200", "Successfully get comments.");
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    //    @Transactional
//    public ResponseDto<?> updateComment(int id, CommentRequestDto requestDto, HttpServletRequest request) {
//        if (request.getHeader("Authorization") == null) {
//            return ResponseDto.fail("400", "AccessToken.");
//        }
//        Member member = validateMember(request);
//        if (null == member){
//            return ResponseDto.fail("400","Member");
//        }
//        Comment comment = isPresentComment(id);
//        if (null == comment){
//            return ResponseDto.fail("400","Already deleted comment.");
//        }
//        if (comment.validateMember(member)){
//            return ResponseDto.fail("400","Modified Author Only");
//        }
//        comment.update(requestDto);
//        return ResponseDto.success(
//                CommentResponseDto.builder()
//                        .id(comment.getId())
//                        .username(member.getUsername())
//                        .userprofile(member.getUserprofile())
//                        .content(comment.getContent())
//                        .Ismine(comment.getMember().getUsername().equals(member.getUsername()))
//                        .build(),"200","Successfully edited comment."
//        );
//    }
    @Transactional
    public ResponseDto<?> updateComment(int id, CommentRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Member> memberCheck = memberRepository.findByUsername(userDetails.getUsername());
        if (memberCheck.isEmpty()) {
            return ResponseDto.fail("400", "fail");
        }

        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("400", "Already deleted comment.");
        }

        if (!comment.getMember().getUsername().equals(memberCheck.get().getUsername())) {
            return ResponseDto.fail("400", "Modified Author Only");
        }
        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .username(comment.getMember().getUsername())
                        .userprofile(comment.getMember().getUserprofile())
                        .content(comment.getContent())
                        .Ismine(comment.getMember().getUsername().equals(memberCheck.get().getUsername()))
                        .build(), "200", "Successfully edited comment."
        );
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(int id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    //    @Transactional
//    public ResponseDto<?> deleteComment(int id, HttpServletRequest request){
//        if (request.getHeader("Authorization") == null) {
//            return ResponseDto.fail("400", "AccessToken.");
//        }
//        Member member = validateMember(request);
//        if (null == member){
//            return ResponseDto.fail("400","Member");
//        }
//        Comment comment = isPresentComment(id);
//        if (null == comment){
//            return ResponseDto.fail("400","Already deleted comment.");
//        }
//        if (comment.validateMember(member)){
//            return ResponseDto.fail("400","Modified Author Only");
//        }
//        commentRepository.delete(comment);
//        return ResponseDto.success(null,"200","Successfully deleted comment.");
//    }
    @Transactional
    public ResponseDto<?> deleteComment(int id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Member> memberCheck = memberRepository.findByUsername(userDetails.getUsername());
        if (memberCheck.isEmpty()) {
            return ResponseDto.fail("400", "fail");
        }
        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("400", "Already deleted comment.");
        }

        if (!comment.getMember().getUsername().equals(memberCheck.get().getUsername())) {
            return ResponseDto.fail("400", "Deleted Author Only");
        }
        commentRepository.delete(comment);
        return ResponseDto.success(null, "200", "Successfully deleted comment.");
    }
}
