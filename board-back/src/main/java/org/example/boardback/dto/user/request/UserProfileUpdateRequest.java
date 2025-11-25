package org.example.boardback.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserProfileUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    private String gender; // Gender enum: MALE / FEMALE / OTHER
}