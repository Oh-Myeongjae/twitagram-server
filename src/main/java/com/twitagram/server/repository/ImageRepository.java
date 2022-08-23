package com.twitagram.server.repository;

import com.twitagram.server.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
//    List<Image> findAllById(int id);
    List<Image> findAllByPost_Id(int id);

//    List<Post> findAllByOrderByModifiedAtDesc(); // 이걸 쓰면 왜   Error creating bean with name 'XXX' 발생했을까??
}
