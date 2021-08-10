package botobo.core.exception;

import botobo.core.exception.common.ErrorResponse;
import botobo.core.exception.common.ErrorType;
import botobo.core.exception.http.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBindingException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        Objects.requireNonNull(fieldError);

        ErrorType errorType = ErrorType.of(fieldError.getDefaultMessage());
        log.info("MethodArgumentNotValidException", e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorType));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> asd(InternalServerErrorException e) {
        log.info("InternalServerErrorException", e.getServerMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorType()));
    }

    @ExceptionHandler(BotoboException.class)
    public ResponseEntity<ErrorResponse> handleBotoboException(BotoboException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ErrorResponse.of(e.getErrorType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException e) {
        String message = String.format("파라미터를 입력해야 합니다.(%s)", e.getParameterName());
        log.info("MethodArgumentNotValidException", e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(message));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
        log.info("MaxUploadSizeExceededException", e);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.of("10MB 이하의 파일만 업로드할 수 있습니다."));
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleSizeLimitExceededException(SizeLimitExceededException e) {
        log.info("SizeLimitExceededException", e);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.of("요청할 수 있는 최대 파일 크기는 100MB 입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.info("Exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of("알 수 없는 예외가 발생했습니다."));
    }
}
