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
public class ProfilePageDto {
    private int currpage;
    private int totalpage;
    private int currcontent;
    private long totalelements;
    private  boolean isme;
    private List<PostResponseDto> posts;
    private List<ProfileInfoDto> following;
    private List<ProfileInfoDto> followers;
}
