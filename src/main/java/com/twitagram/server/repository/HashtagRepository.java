package com.twitagram.server.repository;

import com.twitagram.server.entity.Hashtags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtags, Integer> {
     List<Hashtags> findAllByPost_Id(int id);
//    List<Post> findAllByOrderByModifiedAtDesc(); // 이걸 쓰면 왜   Error creating bean with name 'XXX' 발생했을까??
     Optional<Hashtags> findHashtagsByTags(String tags);
}
