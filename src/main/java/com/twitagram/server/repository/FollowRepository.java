package com.twitagram.server.repository;

import com.twitagram.server.entity.Follow;
import com.twitagram.server.entity.Likes;
import com.twitagram.server.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    int countByMember_IdAndFollow_Id(int member_id,int follow_id);
    int countByMember_Id(int member_id);
    int countByFollow_Id(int follow_id);
    Follow findByMember_IdAndFollow_Id(int member_id,int follow_id);

    Page<Follow> findAllByMember(Member member, Pageable pageable);

    Page<Follow> findAllByFollow(Member Follow, Pageable pageable);
}

