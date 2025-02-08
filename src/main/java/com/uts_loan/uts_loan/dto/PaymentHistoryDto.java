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
public class PaymentHistoryDto {
    private int paymentId;
    private int amount;
    private LocalDate paymentDate;
    private String paymentMethod;
}
