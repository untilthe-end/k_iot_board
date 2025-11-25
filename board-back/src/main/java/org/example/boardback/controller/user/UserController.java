package org.example.boardback.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.boardback.common.apis.user.UserApi;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.entity.file.FileInfo;
import org.example.boardback.service.impl.ProfileServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UserApi.ROOT)
@RequiredArgsConstructor
/*
  사용자 정보 RUD (Create는 Auth 영역)
  : 조회 + 수정 시 프로필 이미지 조회 동시에 처리
 */
public class UserController {
    private final ProfileServiceImpl profileService;

    @PostMapping(UserApi.ME + "/profile")
    public ResponseEntity<ResponseDto<?>> uploadProfile(
//            @AuthenticationPrincipal Long userId,
            @RequestParam("file")MultipartFile file
    ) {
        Long userId = 1L;
        ResponseDto<FileInfo> result = profileService.updateProfile(userId, file);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
