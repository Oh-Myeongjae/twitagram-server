package com.twitagram.server.repository;

import com.twitagram.server.entity.Member;
import com.twitagram.server.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByMember(Member member, Pageable pageable);

    int countByMember_Id(int member_id);
//    List<Post> findAllByOrderByModifiedAtDesc(); // 이걸 쓰면 왜   Error creating bean with name 'XXX' 발생했을까??
}
