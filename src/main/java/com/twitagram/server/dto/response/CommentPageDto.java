package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageDto {
    private int currpage;
    private int totalpage;
    private int currcontent;
    private List<CommentResponseDto> comments;
}
