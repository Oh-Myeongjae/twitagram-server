package com.twitagram.server.repository;

import com.twitagram.server.entity.Follow;
import com.twitagram.server.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    int countByMember_IdAndFollow_Id(int member_id,int follow_id);
}

