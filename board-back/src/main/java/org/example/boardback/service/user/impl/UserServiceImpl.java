package org.example.boardback.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.example.boardback.common.enums.ErrorCode;
import org.example.boardback.dto.ResponseDto;
import org.example.boardback.dto.user.request.UserProfileUpdateRequest;
import org.example.boardback.dto.user.response.MeResponseDto;
import org.example.boardback.dto.user.response.UserResponseDto;
import org.example.boardback.entity.user.User;
import org.example.boardback.exception.BusinessException;
import org.example.boardback.repository.user.UserRepository;
import org.example.boardback.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<MeResponseDto> getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        MeResponseDto dto = MeResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .roles(user.getUserRoles().stream()
                        .map(role -> role.getRole().getName()).collect(Collectors.toSet()))
                .provider(user.getProviderId())
                .build();

        return ResponseDto.success(dto);
    }

    @Override
    public ResponseDto<UserResponseDto> getUserById(Long userId) {
        return null;
    }

    @Override
    public ResponseDto<UserResponseDto> updateProfile(Long userId, UserProfileUpdateRequest request) {
        return null;
    }
}