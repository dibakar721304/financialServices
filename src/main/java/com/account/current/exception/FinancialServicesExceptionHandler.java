package com.account.current.exception;

import com.account.current.model.dto.ExceptionDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FinancialServicesExceptionHandler {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(FinancialServicesExceptionHandler.class);

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(createRuntimeExceptionDto(accountNotFoundException), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InsufficientFundException.class)
    public ResponseEntity<?> handleInsufficientFundException(InsufficientFundException insufficientFundException) {
        return new ResponseEntity<>(createRuntimeExceptionDto(insufficientFundException), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<?> handleCustomerNotFoundExceptionn(CustomerNotFoundException insufficientFundException) {
        return new ResponseEntity<>(createRuntimeExceptionDto(insufficientFundException), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handleJsonMappingException(JsonMappingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> databaseConnectionFailsException(Exception exception) {
        return new ResponseEntity<>(createExceptionDto(exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionDto createRuntimeExceptionDto(RuntimeException financialServiceRuntimeException) {
        log.error("Error occurred {}", financialServiceRuntimeException.getMessage());
        return ExceptionDto.builder()
                .message(financialServiceRuntimeException.getMessage())
                .date(LocalDateTime.now())
                .build();
    }

    private ExceptionDto createExceptionDto(Exception financialServiceException) {
        log.error("Error occurred {}", financialServiceException.getMessage());
        return ExceptionDto.builder()
                .message(financialServiceException.getMessage())
                .date(LocalDateTime.now())
                .build();
    }
}
