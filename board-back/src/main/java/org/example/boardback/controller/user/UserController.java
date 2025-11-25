package org.example.boardback.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.boardback.common.apis.user.UserApi;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.user.request.UserProfileUpdateRequest;
import org.example.boardback.dto.user.response.MeResponseDto;
import org.example.boardback.dto.user.response.UserResponseDto;
import org.example.boardback.entity.file.FileInfo;
import org.example.boardback.security.user.UserPrincipal;
import org.example.boardback.service.impl.ProfileServiceImpl;
import org.example.boardback.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UserApi.ROOT)
@RequiredArgsConstructor
/*
  사용자 정보 RUD (Create는 Auth 영역)
  : 조회 + 수정 시 프로필 이미지 조회 동시에 처리
 */
public class UserController {
    private final UserService userService;
    private final ProfileServiceImpl profileService;

    /**
     * 내 정보 조회
     */
    @GetMapping(UserApi.ME) // GET /api/v1/users/me
    public ResponseEntity<ResponseDto<MeResponseDto>> me(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<MeResponseDto> result = userService.getMe(userPrincipal.getId());
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    /**
     * 특정 사용자 정보 조회
     */
    @GetMapping(UserApi.ID_ONLY) // GET /api/v1/users/{userId}
    public ResponseEntity<ResponseDto<UserResponseDto>> getById(
            @PathVariable Long userId
    ) {
        ResponseDto<UserResponseDto> result = userService.getUserById(userId);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    /**
     * 사용자 정보 수정 (닉네임 등)
     */
    @PutMapping(UserApi.PROFILE) // PUT /api/v1/users/profile
    public ResponseEntity<ResponseDto<UserResponseDto>> updateProfile(
            @RequestAttribute("userId") Long userId,
            @RequestBody UserProfileUpdateRequest request
    ) {
        ResponseDto<UserResponseDto> result = userService.updateProfile(userId, request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @PostMapping(UserApi.PROFILE_IMAGE)
    public ResponseEntity<ResponseDto<?>> uploadProfile(
            @AuthenticationPrincipal Long userId,
            @RequestParam("file")MultipartFile file
    ) {
        ResponseDto<FileInfo> result = profileService.updateProfile(userId, file);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}