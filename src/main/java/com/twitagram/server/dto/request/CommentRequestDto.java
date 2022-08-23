package com.twitagram.server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    //private int post_id;
    private String content;
    private String[] hashtags;  //HashTags 해야 할 것 같은데 일단 api 명세에 적힌대로 썼습니다

}