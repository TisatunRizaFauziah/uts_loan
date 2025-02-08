package com.uts_loan.uts_loan.sevices;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.uts_loan.uts_loan.dto.LoanDto;
import com.uts_loan.uts_loan.dto.LoanHistoryDto;
import com.uts_loan.uts_loan.dto.LoanReportByCustomerTypeDto;
import com.uts_loan.uts_loan.dto.LoanReportDto;
import com.uts_loan.uts_loan.dto.LoanStatusDto;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.UpdateLoanDto;
import com.uts_loan.uts_loan.models.Loan;

public interface LoanService {
    
    PageResponse<LoanDto> findAll(Integer customerId, String tenor, String status, Pageable pageable);
    Loan create(LoanDto dto);

    void update(int id, UpdateLoanDto dto);

    void delete(int id);

    LoanReportDto getLoanReport();

    LoanReportByCustomerTypeDto getLoanReportByCustomerType(String Customer);

    List<LoanHistoryDto> getPersonalLoanHistory(String accountNumber);

    List<LoanStatusDto> findActiveLoans();
}
