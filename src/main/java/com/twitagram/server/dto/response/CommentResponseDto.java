package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Integer id;
    private String username;
    private String userprofile;
    private String content;
    private Boolean Ismine;
}
