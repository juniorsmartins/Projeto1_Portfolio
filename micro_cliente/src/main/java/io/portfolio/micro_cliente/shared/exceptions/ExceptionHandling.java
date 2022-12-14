package io.portfolio.micro_cliente.shared.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public final class ExceptionHandling {

    @Autowired
    private MessageSource internationalMessage;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<StandardExceptionHandledReturn>> methodArgumentNotValidException(MethodArgumentNotValidException method) {

        List<StandardExceptionHandledReturn> errors = new ArrayList<>();
        method.getBindingResult().getFieldErrors().forEach(err -> {
            String message = internationalMessage.getMessage(err, LocaleContextHolder.getLocale());
            errors.add(new StandardExceptionHandledReturn(HttpStatus.BAD_REQUEST.toString(), err.getCode(), err.getField(), message));
        });

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(ResourceNotFoundCustomException.class)
    public ResponseEntity<StandardExceptionHandledReturn> methodStandardExceptionHandledReturn(ResourceNotFoundCustomException resource) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StandardExceptionHandledReturn(HttpStatus.NOT_FOUND.toString(), resource.getMessage()));
    }

    @ExceptionHandler(BusinessRuleViolationCustomException.class)
    public ResponseEntity<StandardExceptionHandledReturn> methodBusinessRuleViolationCustomException(BusinessRuleViolationCustomException businessViolation) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new StandardExceptionHandledReturn(HttpStatus.CONFLICT.toString(), businessViolation.getMessage()));
    }

    @ExceptionHandler(InternalErrorCustomException.class)
    public ResponseEntity<StandardExceptionHandledReturn> methodInternalErrorCustomException(InternalErrorCustomException internal) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new StandardExceptionHandledReturn(HttpStatus.INTERNAL_SERVER_ERROR.toString(), internal.getMessage()));
    }
}

