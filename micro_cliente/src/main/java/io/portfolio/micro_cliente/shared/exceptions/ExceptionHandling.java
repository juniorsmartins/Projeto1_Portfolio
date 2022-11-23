package io.portfolio.micro_cliente.shared.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public final class ExceptionHandling {

    @Autowired
    private MessageSource internationalMessage;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardExceptionHandledReturn> methodArgumentNotValidException(MethodArgumentNotValidException method) {
        List<StandardExceptionHandledReturn> listOfHandledErrors = new ArrayList<>();
        List<FieldError> listOfFieldErrors = method.getBindingResult().getFieldErrors();
        listOfFieldErrors.forEach(theError -> {
            String message = internationalMessage.getMessage(theError, LocaleContextHolder.getLocale());
            StandardExceptionHandledReturn exceptionHandledReturn = new StandardExceptionHandledReturn(
                    HttpStatus.BAD_REQUEST.toString(), message, theError.getCode(), theError.getField());
            listOfHandledErrors.add(exceptionHandledReturn);
        });
        return ResponseEntity
                .badRequest()
                .body(listOfHandledErrors.get(0));
    }

    @ExceptionHandler(ResourceNotFoundCustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardExceptionHandledReturn> methodStandardExceptionHandledReturn(ResourceNotFoundCustomException resource) {
        return ResponseEntity
                .badRequest()
                .body(new StandardExceptionHandledReturn(HttpStatus.NOT_FOUND.toString(), resource.getMessage()));
    }

}

