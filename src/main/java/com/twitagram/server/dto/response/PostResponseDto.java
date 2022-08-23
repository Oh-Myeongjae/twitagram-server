package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private int id;
    private String username;
    private String userprofile;
    private String content;
    private List<String> imageurls;
    private List<String> hashtags;
    private boolean Ismine;
    private boolean Isliked;
    private boolean Isfollowing;
    private int numcomments;
    private int numlikes;
    private String time;
}
