package com.uts_loan.uts_loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LoanReportDto {
    private int totalPaid;
    private int totalUnpaid;
}
