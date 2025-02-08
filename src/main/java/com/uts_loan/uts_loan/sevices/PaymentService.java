package com.uts_loan.uts_loan.sevices;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.PaymentDto;
import com.uts_loan.uts_loan.dto.UpdatePaymentDto;
import com.uts_loan.uts_loan.models.Payment;

public interface PaymentService {
    
    PageResponse<PaymentDto> findAll(Integer loanId, LocalDate paymentDate, String paymentMethod, Pageable pageable);

    Payment create(PaymentDto dto);

    void delete(int id);

    void update(int id, UpdatePaymentDto dto);
}
