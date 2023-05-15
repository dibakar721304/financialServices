package com.account.current.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FinancialServicesExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(CustomerNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InsufficientFundException.class)
    public ResponseEntity<?> insufficientFundException(InsufficientFundException insufficientFundException) {
        return new ResponseEntity<>(insufficientFundException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleConverterErrors(JsonMappingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleConverterErrors(HttpMessageNotReadableException exception) {
        var message = exception.getMessage() != null
                        && !exception.getMessage().isEmpty()
                        && exception.getMessage().contains("com.tuum.bankassignment.entity.Currency")
                ? "Invalid Currency"
                : "Bad request";
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
        return new ResponseEntity<>("There is a problem", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
