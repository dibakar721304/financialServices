package com.account.current.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExceptionDto {
    private String message;
    private LocalDateTime date;
}
