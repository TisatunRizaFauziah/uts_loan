package com.uts_loan.uts_loan.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.PaymentDto;
import com.uts_loan.uts_loan.dto.UpdatePaymentDto;
import com.uts_loan.uts_loan.models.Payment;
import com.uts_loan.uts_loan.sevices.PaymentService;

@RestController
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

     @PostMapping("/create")
    public ResponseEntity<GenericResponse<Object>> create(@RequestBody PaymentDto dto){
        Payment newPayment = paymentService.create(dto);
        return ResponseEntity.ok().body(GenericResponse.builder()
                            .success(true)
                            .message("Data berhasil di tambahkan")
                            .data(newPayment)
                            .build());
    }

     @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse<Object>> delete(@PathVariable int id) {
        paymentService.delete(id);
        return ResponseEntity.ok(GenericResponse.builder()
                .success(true)
                .message("Data berhasil dihapus")
                .data(null)
                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<Object>> updatePayment(@PathVariable int id,
                                                                 @RequestBody UpdatePaymentDto dto) {
        try {
            paymentService.update(id, dto);
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
                .message("Data payment berhasil diperbarui")
                .data(null)
                .build());
    }
    @GetMapping("/find-all")
    public ResponseEntity<GenericResponse<PageResponse<PaymentDto>>> findAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Integer loanId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
            @RequestParam(required = false) String paymentMethod) {
    
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PaymentDto> response = paymentService.findAll(loanId, paymentDate, paymentMethod, pageable);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.<PageResponse<PaymentDto>>builder()
                    .success(false)
                    .message("Data tidak ditemukan")
                    .data(null)
                    .build());
        }
    
        return ResponseEntity.ok().body(GenericResponse.<PageResponse<PaymentDto>>builder()
                .success(true)
                .message("Data berhasil diambil")
                .data(response)
                .build());
    }
    

}
