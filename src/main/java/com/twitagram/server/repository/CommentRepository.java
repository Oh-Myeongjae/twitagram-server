package com.twitagram.server.repository;

import com.twitagram.server.entity.Comment;
import com.twitagram.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Integer> {

//    List<Comment> findAllByPost(Post post, Pageable pageable);

    Page<Comment> findAllByPost(Post post, Pageable pageable);
}
