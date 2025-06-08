package com.rafaelsousa.post.service.api.exception.handler;

import com.rafaelsousa.post.service.api.exception.ExceptionResponse;
import com.rafaelsousa.post.service.api.exception.ExceptionResponseType;
import com.rafaelsousa.post.service.api.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static com.rafaelsousa.post.service.api.exception.ExceptionResponseType.INVALID_DATA;

@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handle(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String detail = ex.getMessage();

        ExceptionResponse response = getExceptionResponseBuilder(status, ExceptionResponseType.RESOURCE_NOT_FOUND, detail)
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex
            , @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        return getObjectResponseEntity(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body
            , @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        if (Objects.isNull(body)) {
            body = ExceptionResponse.builder()
                    .status(status.value())
                    .timestamp(OffsetDateTime.now())
                    .title(HttpStatus.valueOf(status.value()).getReasonPhrase())
                    .build();
        } else if (body instanceof String message) {
            body = ExceptionResponse.builder()
                    .status(status.value())
                    .timestamp(OffsetDateTime.now())
                    .title(message)
                    .build();
        }

        return Objects.requireNonNull(super.handleExceptionInternal(ex, body, headers, status, request));
    }

    private static ExceptionResponse.ExceptionResponseBuilder getExceptionResponseBuilder(HttpStatusCode status
            , ExceptionResponseType exceptionResponseType, String detail) {
        return ExceptionResponse.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(exceptionResponseType.getUri())
                .title(exceptionResponseType.getTitle())
                .detail(detail);
    }

    private ResponseEntity<Object> getObjectResponseEntity(Exception ex, BindingResult bindingResult, HttpHeaders headers
            , HttpStatusCode status, WebRequest request) {
        String detail = "One or more fields are invalid. Please fill in correctly and try again.";

        List<ExceptionResponse.Object> errorObjects = bindingResult.getAllErrors()
                .stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String fieldName = objectError.getObjectName();

                    if (objectError instanceof FieldError fieldError)
                        fieldName = fieldError.getField();

                    return ExceptionResponse.Object.builder()
                            .field(fieldName)
                            .validation(message)
                            .build();
                }).toList();

        ExceptionResponse response = getExceptionResponseBuilder(status, INVALID_DATA, detail)
                .objects(errorObjects)
                .build();

        return handleExceptionInternal(ex, response, headers, status, request);
    }
}