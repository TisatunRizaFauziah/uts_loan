package com.uts_loan.uts_loan.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.uts_loan.uts_loan.models.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer>, JpaSpecificationExecutor<Loan> {
      List<Loan> findByStatus(String status);

      List<Loan> findByCustomerId(int loanId);

      Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

      @Query("SELECT SUM(l.amount - l.remain) FROM Loan l")
      Integer sumTotalPaid();

      @Query("SELECT SUM(l.remain) FROM Loan l")
      Integer sumTotalUnpaid();
}
