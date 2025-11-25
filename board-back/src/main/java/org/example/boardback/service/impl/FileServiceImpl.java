package org.example.boardback.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.boardback.common.enums.ErrorCode;
import org.example.boardback.entity.file.FileInfo;
import org.example.boardback.exception.FileStorageException;
import org.example.boardback.repository.file.FileInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {
    // application.properties 파일에서 주입되는 기본 저장 경로
    @Value("${file.upload.base-path}")
    private String basePath;

    // 사용자 프로필 이미지 저장용 상대 경로
    @Value("${file.upload.user-profile}")
    private String profilePath;

    // 게시판 파일 저장의 루트 상대 경로
    @Value("${file.upload.board}")
    private String boardRootPath;

    private final FileInfoRepository fileInfoRepository;

    // 허용할 확장자 (상황에 따라 조정)
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "gif",
            "pdf", "txt", "zip"
    );

    // 단일 파일 최대 크기 (예: 10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    /** 파일 유효성 검증 */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException(ErrorCode.INVALID_INPUT, "빈 파일은 업로드할 수 없습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException(
                    ErrorCode.INVALID_INPUT,
                    "파일 용량이 너무 큽니다. 최대 10MB까지 업로드 가능합니다."
            );
        }

        String original = file.getOriginalFilename();
        String cleanName = StringUtils.cleanPath(original != null ? original : "");

        // 경로 조작 방지
        if (cleanName.contains("..")) {
            throw new FileStorageException(
                    ErrorCode.INVALID_INPUT,
                    "잘못된 파일 이름입니다."
            );
        }

        // 확장자 체크
        String ext = "";
        int dot = cleanName.lastIndexOf('.');
        if (dot != -1 && dot < cleanName.length() - 1) {
            ext = cleanName.substring(dot + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new FileStorageException(
                    ErrorCode.INVALID_INPUT,
                    "허용되지 않는 파일 형식입니다."
            );
        }
    }

    /** 업로드 디렉토리 생성 */
    private void ensureDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            // 업로드하려는 경로가 실제 디렉토리에 존재하지 않으면
            // cf) mkdirs(): 존재하지 않는 상위 디렉토리까지 모두 생성
            dir.mkdirs();
        }
    }

    /** 저장 파일명 생성 */
    private String generateStoredName(String originalName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 강아지
        // qwerty_강아지
        return uuid + "_" + originalName;
    }

    /** 실제 업로드 경로 생성 */
    private String buildFullPath(String relativePath, String storedName) {
        return basePath + "/" + relativePath + "/" + storedName;
    }

    /** 프로필 업로드 (1개만 유지) */
    public FileInfo saveUserProfileImage(MultipartFile file) {
//        long maxSize = 5 * 1024 * 1024; // 5MB
//
//        if (file.getSize() > maxSize) {
//            throw new IllegalArgumentException("파일 크기가 제한을 초과했습니다.");
//        }

        // 프로필은 파일이 선택 안 된 경우도 있을 수 있음
        if (file == null || file.isEmpty()) return null;

        // ✅ 파일 검증 추가
        validateFile(file);

        try {
            String original = file.getOriginalFilename();
            String cleanName = StringUtils.cleanPath(original);
            String storedName = generateStoredName(cleanName);

            String fullDir = basePath + "/" + profilePath;
            ensureDirectory(fullDir);

            // Paths.get()으로 경로값을 받아 Path 객체 생성
            Path path = Paths.get(fullDir + "/" + storedName);
            // InputStream 으로 데이터를 읽어 path 위치에 파일을 생성 (또는 덮어쓰기)
            // +) REPLACE_EXISTING: 덮어쓰기 옵션
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            FileInfo info = FileInfo.builder()
                    .originalName(cleanName)
                    .storedName(storedName)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(path.toString())
                    .createdAt(LocalDateTime.now())
                    .build();

            return fileInfoRepository.save(info);

        } catch (Exception e) {
            throw new FileStorageException(ErrorCode.INTERNAL_ERROR, "" ,e);
        }
    }

    /** 게시글 파일 업로드 */
    public FileInfo saveBoardFile(Long boardId, MultipartFile file) {
        // 업로드할 파일이 아예 없을 수도 있음
        if (file == null || file.isEmpty()) {
            return null;
        }

        // ✅ 검증
        validateFile(file);

        try {
            String original = file.getOriginalFilename();
            String cleanName = StringUtils.cleanPath(original);
            String storedName = generateStoredName(cleanName);

            String relativePath = boardRootPath + "/" + boardId;
            String fullDir = basePath + "/" + relativePath;

            ensureDirectory(fullDir);

            Path path = Paths.get(fullDir + "/" + storedName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            FileInfo info = FileInfo.builder()
                    .originalName(cleanName)
                    .storedName(storedName)
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .filePath(path.toString())
                    .createdAt(LocalDateTime.now())
                    .build();

            return fileInfoRepository.save(info);

        } catch (Exception e) {
            throw new FileStorageException(ErrorCode.INTERNAL_ERROR, "" ,e);
        }
    }

    /** 파일 삭제 */
    @Transactional
    public void deleteFile(FileInfo info) {
        try {
            Path path = Paths.get(info.getFilePath());
            Files.deleteIfExists(path);
        } catch (Exception e) {
            throw new FileStorageException(ErrorCode.INTERNAL_ERROR, "" ,e);
        }
        fileInfoRepository.delete(info);
    }
}
