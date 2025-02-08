package com.uts_loan.uts_loan.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LoanHistoryDto {
    private int loanId;
    private int amount;
    private int remain;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String tenor;
    private String status;
    private List<PaymentHistoryDto> paymentHistory;
}
