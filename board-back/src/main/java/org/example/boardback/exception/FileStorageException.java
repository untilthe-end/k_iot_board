package org.example.boardback.exception;

import lombok.Getter;
import org.example.boardback.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class FileStorageException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage; // 추가 메시지(Optional)


    /**
     * 기본 FileStorageException
     * - ErrorCode 메시지를 클라이언트에 노출
     */
    public FileStorageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    /**
     * 상세 메시지 포함
     * - detailMessage는 서버 로그용
     * - 클라이언트에는 ErrorCode 메시지만 전달
     */
    public FileStorageException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    /**
     * root cause 포함
     */
    public FileStorageException(ErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    @Override
    public String toString() {
        return "[FileStorageException] " + errorCode +
                (detailMessage != null ? " / detail: " + detailMessage : "");
    }
}
