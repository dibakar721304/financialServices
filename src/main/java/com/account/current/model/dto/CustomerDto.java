package com.account.current.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    private String name;
    private String email;
    private String surName;
    private List<CurrentAccountDto> currentAccountDtoList;
    private String message;
}
