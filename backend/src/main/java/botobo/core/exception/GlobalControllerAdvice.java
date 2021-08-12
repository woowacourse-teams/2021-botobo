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

        log.info(String.format("MethodArgumentNotValidException %s", e.getMessage()), e);
        ErrorType errorType = ErrorType.of(fieldError.getDefaultMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorType));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException e) {
        log.info(String.format("InternalServerErrorException %s", e.getServerMessage()), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getErrorType()));
    }

    @ExceptionHandler(BotoboException.class)
    public ResponseEntity<ErrorResponse> handleBotoboException(BotoboException e) {
        log.info(String.format("%s %s", e.getClass().getName(), e.getMessage()), e);
        return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.of(e.getErrorType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException e) {
        log.info(String.format("MissingServletRequestParameterException %s", e.getMessage()), e);
        String message = String.format("파라미터를 입력해야 합니다.(%s)", e.getParameterName());
        return ResponseEntity.badRequest().body(ErrorResponse.of("E001", message));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
        log.info(String.format("MaxUploadSizeExceededException %s", e.getMessage()), e);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.of(ErrorType.U010));
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleSizeLimitExceededException(SizeLimitExceededException e) {
        log.info(String.format("SizeLimitExceededException %s", e.getMessage()), e);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.of(ErrorType.U011));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.info(String.format("Exception %s", e.getMessage()), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(ErrorType.X001));
    }
}
