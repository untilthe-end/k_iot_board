package org.example.boardback.dto.user.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileImageResponseDto {
    private String profileImageUrl;
}