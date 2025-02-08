package com.uts_loan.uts_loan.sevices;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.PaymentDto;
import com.uts_loan.uts_loan.dto.UpdatePaymentDto;
import com.uts_loan.uts_loan.models.Loan;
import com.uts_loan.uts_loan.models.Payment;
import com.uts_loan.uts_loan.repositories.LoanRepository;
import com.uts_loan.uts_loan.repositories.PaymentRepository;
import com.uts_loan.uts_loan.repositories.specification.PaymentSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, LoanRepository loanRepository) {
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;

    }

    @Override
    public Payment create(PaymentDto dto) {
        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan Tidak ada"));

        Payment payment = mapToPayment(dto, loan);
        return paymentRepository.save(payment);
    }

    public Payment mapToPayment(PaymentDto dto, Loan loan) {
        return Payment.builder()
                .id(dto.getId())
                .loan(loan)
                .amount(dto.getAmount())
                .paymentDate(LocalDate.now())
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }

    public PaymentDto mapToPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .loanId(payment.getLoan().getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }

    @Override
    public void delete(int id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        payment.ifPresent(paymentRepository::delete);

    }

    @Override
    public void update(int id, UpdatePaymentDto dto) {
        Payment paymentToUpdate = paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Payment dengan ID {} tidak ditemukan", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment tidak ditemukan");
                });

        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan terkait tidak ditemukan"));


        paymentToUpdate.setLoan(loan);
        paymentToUpdate.setAmount(dto.getAmount());
        paymentToUpdate.setPaymentDate(dto.getPaymentDate());
        paymentToUpdate.setPaymentMethod(dto.getPaymentMethod());

        paymentRepository.save(paymentToUpdate);
    }

    public PageResponse<PaymentDto> findAll(Integer loanId, LocalDate paymentDate, String paymentMethod,
            Pageable pageable) {
        Specification<Payment> spec = Specification.where(null);

        if (loanId != null && loanId > 0) {
            spec = spec.and(PaymentSpecification.filterLoanId(loanId));
        }
        if (paymentDate != null) {
            spec = spec.and(PaymentSpecification.filterPaymentDate(paymentDate));
        }
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            spec = spec.and(PaymentSpecification.filterPaymentMethod(paymentMethod));
        }

        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);

        return PageResponse.<PaymentDto>builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(paymentPage.getTotalPages())
                .totalItem(paymentPage.getTotalElements())
                .items(paymentPage.stream()
                        .map(this::mapToPaymentDto)
                        .toList())
                .build();
    }

}
