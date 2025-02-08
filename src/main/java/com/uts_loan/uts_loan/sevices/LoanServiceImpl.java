package com.uts_loan.uts_loan.sevices;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uts_loan.uts_loan.dto.LoanDto;
import com.uts_loan.uts_loan.dto.LoanHistoryDto;
import com.uts_loan.uts_loan.dto.LoanReportByCustomerTypeDto;
import com.uts_loan.uts_loan.dto.LoanReportDto;
import com.uts_loan.uts_loan.dto.LoanStatusDto;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.PaymentHistoryDto;
import com.uts_loan.uts_loan.dto.UpdateLoanDto;
import com.uts_loan.uts_loan.models.Customer;
import com.uts_loan.uts_loan.models.Loan;
import com.uts_loan.uts_loan.models.Payment;
import com.uts_loan.uts_loan.repositories.CustomerRepository;
import com.uts_loan.uts_loan.repositories.LoanRepository;
import com.uts_loan.uts_loan.repositories.PaymentRepository;
import com.uts_loan.uts_loan.repositories.specification.LoanSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository; // Gunakan LoanRepository
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository,
            CustomerRepository customerRepository,
            PaymentRepository paymentRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    private Loan mapToLoan(LoanDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan"));

        return Loan.builder()
                .id(dto.getLoanId())
                .customer(customer)
                .amount(dto.getAmount())
                .remain(dto.getRemain())
                .startDate(dto.getStartDate())
                .dueDate(dto.getDueDate())
                .tenor(dto.getTenor())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public Loan create(LoanDto dto) {
        Loan loan = mapToLoan(dto);
        return loanRepository.save(loan);
    }

    @Override
    public void delete(int id) {
        if (!loanRepository.existsById(id)) {
            throw new RuntimeException("Loan tidak ditemukan");
        }

        List<Payment> payments = paymentRepository.findByLoanId(id);
        if (!payments.isEmpty()) {
            paymentRepository.deleteAll(payments);
        }

        loanRepository.deleteById(id);
    }

    @Override
    public void update(int id, UpdateLoanDto dto) {
        Loan loanToUpdate = loanRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Loan dengan ID {} tidak ditemukan", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan tidak ditemukan");
                });

        loanToUpdate.setAmount(dto.getAmount());
        loanToUpdate.setRemain(dto.getRemain());
        loanToUpdate.setStartDate(dto.getStartDate());
        loanToUpdate.setDueDate(dto.getDueDate());
        loanToUpdate.setTenor(dto.getTenor());
        loanToUpdate.setStatus(dto.getStatus());

        loanRepository.save(loanToUpdate);
    }

    @Override
    public PageResponse<LoanDto> findAll(int customerId, String tenor, String status, Pageable pageable) {
        Specification<Loan> spec = Specification.where(null);

        if (customerId > 0) {
            spec = spec.and(LoanSpecification.filterCustomerId(customerId));
        }
        if (tenor != null && !tenor.isEmpty()) {
            spec = spec.and(LoanSpecification.filterTenor(tenor));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and(LoanSpecification.filterStatus(status));
        }

        Page<Loan> loanPage = loanRepository.findAll(spec, pageable);

        return PageResponse.<LoanDto>builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(loanPage.getTotalPages())
                .totalItem(loanPage.getTotalElements())
                .items(loanPage.stream()
                        .map(this::mapToLoanDto)
                        .toList())
                .build();
    }

    private LoanDto mapToLoanDto(Loan loan) {
        if (loan.getCustomer() == null) {
            throw new RuntimeException("Customer tidak ditemukan");
        }

        return LoanDto.builder()
                .loanId(loan.getId())
                .customerId(loan.getCustomer().getId())
                .amount(loan.getAmount())
                .remain(loan.getRemain())
                .startDate(loan.getStartDate())
                .dueDate(loan.getDueDate())
                .tenor(loan.getTenor())
                .status(loan.getStatus())
                .build();
    }

    public List<LoanStatusDto> findActiveLoans() {
        List<Loan> loans = loanRepository.findByStatus("active");
        return loans.stream()
                .map(this::mapToLoanStatus)
                .collect(Collectors.toList());
    }
    
    private LoanStatusDto mapToLoanStatus(Loan loan) {
        Customer customer = Optional.ofNullable(loan.getCustomer())
                .orElseThrow(() -> new RuntimeException("Customer not found for loan ID: " + loan.getId()));
    
        return LoanStatusDto.builder()
                .loanId(loan.getId())
                .customerId(customer.getId())
                .accountNumber(customer.getAccountNumber())
                .customerName(customer.getCustomerName())
                .amount(loan.getAmount())
                .remain(loan.getRemain())
                .startDate(loan.getStartDate())
                .dueDate(loan.getDueDate())
                .status(loan.getStatus())
                .tenor(loan.getTenor())
                .build();
    }
    
  
    @Override
    public LoanReportDto getLoanReport() {
        List<Loan> loans = loanRepository.findAll(); 
        int totalPaid = paymentRepository.getTotalPaidAmount(); 
        int totalUnpaid = loans.stream().mapToInt(Loan::getRemain).sum(); 
    
        return new LoanReportDto(totalPaid, totalUnpaid);
    }
    
    @Override
    public LoanReportByCustomerTypeDto getLoanReportByCustomerType(String customerType) {
        List<Loan> loans = loanRepository.findAll(); 

        int totalPaid = 0;
        int totalUnpaid = 0;

        for (Loan loan : loans) {
            if (loan.getCustomer().getCustomerType().equalsIgnoreCase(customerType)) {
                totalPaid += loan.getAmount() - loan.getRemain(); 
                totalUnpaid += loan.getRemain(); 
            }
        }

        return new LoanReportByCustomerTypeDto(customerType, totalPaid, totalUnpaid);
    }

    @Override
    public List<LoanHistoryDto> getPersonalLoanHistory(String accountNumber) {
      
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Customer with account number " + accountNumber + " not found"));
    
        List<Loan> loans = loanRepository.findByCustomerId(customer.getId());
    
        List<LoanHistoryDto> loanHistories = new ArrayList<>();
    
        for (Loan loan : loans) {
            List<Payment> payments = paymentRepository.findByLoanId(loan.getId());
            List<PaymentHistoryDto> paymentHistory = new ArrayList<>();
    
            for (Payment payment : payments) {
                paymentHistory.add(new PaymentHistoryDto(
                        payment.getId(),
                        payment.getAmount(),
                        payment.getPaymentDate(),
                        payment.getPaymentMethod()));
            }

            loanHistories.add(new LoanHistoryDto(
                    loan.getId(),
                    loan.getAmount(),
                    loan.getRemain(),
                    loan.getStartDate(),
                    loan.getDueDate(),
                    loan.getTenor(),
                    loan.getStatus(),
                    paymentHistory));
        }
    
        return loanHistories;
    }
}    