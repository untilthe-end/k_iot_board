package org.example.boardback.dto.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private String gender;
}