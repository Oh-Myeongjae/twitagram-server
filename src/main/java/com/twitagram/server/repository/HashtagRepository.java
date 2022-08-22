package com.twitagram.server.repository;

import com.twitagram.server.entity.Hashtags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtags, Integer> {
//    List<Post> findAllByOrderByModifiedAtDesc(); // 이걸 쓰면 왜   Error creating bean with name 'XXX' 발생했을까??
}
