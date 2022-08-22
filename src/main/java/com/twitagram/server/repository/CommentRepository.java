package com.twitagram.server.repository;

import com.twitagram.server.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Integer> {
}
