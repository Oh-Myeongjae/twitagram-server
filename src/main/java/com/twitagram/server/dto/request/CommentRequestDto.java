package com.twitagram.server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    //private int post_id;
    private String content;
    private List<String> hashtags;

}