package com.twitagram.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

//    //좋아요
//    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<PostLike> postLikeList;


//    public void update(PostRequestDto postRequestDto, String imageUrl, Member member){
//        this.title = postRequestDto.getTitle();
//        this.content = postRequestDto.getContent();
//        this.imageUrl = imageUrl;
//        this.member = member;
//    }

}
