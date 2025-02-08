package com.uts_loan.uts_loan.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uts_loan.uts_loan.models.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByLoanId(int loanId);

    Page<Payment> findAll(Specification<Payment> spec, Pageable pageable);
}
