package com.twitagram.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileInfoDto {
    private String username;
    private String userprofile;
    private int numposts;
    private int numfollowing;
    private int numfollowers;
}
