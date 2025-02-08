package com.uts_loan.uts_loan.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uts_loan.uts_loan.dto.GenericResponse;
import com.uts_loan.uts_loan.dto.LoanDto;
import com.uts_loan.uts_loan.dto.LoanHistoryDto;
import com.uts_loan.uts_loan.dto.LoanReportByCustomerTypeDto;
import com.uts_loan.uts_loan.dto.LoanReportDto;
import com.uts_loan.uts_loan.dto.LoanStatusDto;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.UpdateLoanDto;
import com.uts_loan.uts_loan.models.Loan;
import com.uts_loan.uts_loan.sevices.LoanService;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/create")
    public ResponseEntity<GenericResponse<Object>> create(@RequestBody LoanDto dto) {
        Loan newLoan = loanService.create(dto);
        return ResponseEntity.ok().body(GenericResponse.builder()
                .success(true)
                .message("Data berhasil ditambahkan")
                .data(newLoan)
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse<Object>> delete(@PathVariable int id) {
        try {
            loanService.delete(id);
            return ResponseEntity.ok(GenericResponse.builder()
                    .success(true)
                    .message("Data berhasil dihapus")
                    .data(null)
                    .build());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                    .success(false)
                    .message("Terjadi kesalahan di sistem internal")
                    .data(null)
                    .build());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<Object>> updateLoan(@PathVariable int id,
            @RequestBody UpdateLoanDto dto) {
        try {
            loanService.update(id, dto);
        } catch (ResponseStatusException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(ex.getStatusCode()).body(GenericResponse.builder()
                    .success(false)
                    .message(ex.getReason())
                    .data(null)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                    .success(false)
                    .message("Terjadi kesalahan di sistem internal")
                    .data(null)
                    .build());
        }

        return ResponseEntity.ok().body(GenericResponse.builder()
                .success(true)
                .message("Data loan berhasil diperbarui")
                .data(null)
                .build());
    }

    @GetMapping("/find-all")
    public ResponseEntity<GenericResponse<PageResponse<LoanDto>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) String tenor,
            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<LoanDto> response = loanService.findAll(customerId, tenor, status, pageable);

        if (response.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.<PageResponse<LoanDto>>builder()
                    .success(false)
                    .message("Data tidak ditemukan")
                    .data(null)
                    .build());
        }

        return ResponseEntity.ok().body(GenericResponse.<PageResponse<LoanDto>>builder()
                .success(true)
                .message("Data berhasil diambil")
                .data(response)
                .build());
    }

    @GetMapping("/loan-active")
    public ResponseEntity<GenericResponse<List<LoanStatusDto>>> getActiveLoans() {
        List<LoanStatusDto> activeLoans = loanService.findActiveLoans();

        return ResponseEntity.ok(GenericResponse.<List<LoanStatusDto>>builder()
                .success(true)
                .message("Berhasil memuat data")
                .data(activeLoans)
                .build());
    }

    @GetMapping("/loan-report")
    public ResponseEntity<GenericResponse<LoanReportDto>> getLoanReport() {
        LoanReportDto loanReport = loanService.getLoanReport();

        return ResponseEntity.ok(GenericResponse.<LoanReportDto>builder()
                .success(true)
                .message("Berhasil memuat data")
                .data(loanReport)
                .build());
    }

    @GetMapping("/loan-report-by-customer-type")
    public ResponseEntity<GenericResponse<LoanReportByCustomerTypeDto>> getLoanReportByCustomerType(
            @RequestParam String customerType) {

        LoanReportByCustomerTypeDto loanReport = loanService.getLoanReportByCustomerType(customerType);

        return ResponseEntity.ok(GenericResponse.<LoanReportByCustomerTypeDto>builder()
                .success(true)
                .message("Berhasil memuat data")
                .data(loanReport)
                .build());
    }

    @GetMapping("/personal-loan-history")
    public ResponseEntity<GenericResponse<List<LoanHistoryDto>>> getPersonalLoanHistory(
            @RequestParam String accountNumber) {

        List<LoanHistoryDto> loanHistory = loanService.getPersonalLoanHistory(accountNumber);

        return ResponseEntity.ok(GenericResponse.<List<LoanHistoryDto>>builder()
                .success(true)
                .message("Berhasil memuat data")
                .data(loanHistory)
                .build());
    }

}
