package com.twitagram.server.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String content;
    private List<String> hashtags;
    private List<MultipartFile> imagefiles;
}
