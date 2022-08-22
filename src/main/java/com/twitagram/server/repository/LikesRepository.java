package com.twitagram.server.repository;

import com.twitagram.server.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    int countAllByPost_Id(int id);
    Likes findByMember_Id(int id);
}

