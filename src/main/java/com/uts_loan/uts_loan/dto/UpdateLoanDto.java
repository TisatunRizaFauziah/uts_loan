package com.uts_loan.uts_loan.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UpdateLoanDto {

    private int idCustomer;
    private int amount;
    private int remain;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String tenor;
    private String status;

}
