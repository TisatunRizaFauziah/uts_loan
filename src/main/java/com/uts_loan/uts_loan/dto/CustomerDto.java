package com.uts_loan.uts_loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CustomerDto {

    private Integer id;
    private String accountNumber;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String customerType;

}