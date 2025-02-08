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
public class LoanDto{
    private Integer loanId;
    private int customerId;
    private String accountNumber;
    private String customerName;
    private int amount;
    private int remain;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String tenor;
    private String status;
  
}
