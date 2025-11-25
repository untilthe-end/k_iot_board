package org.example.boardback.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import org.example.boardback.common.enums.RoleType;

import java.util.Set;

@Getter
@Builder
public class MeResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Set<RoleType> roles;
    private String provider;
}