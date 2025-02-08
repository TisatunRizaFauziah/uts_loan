package com.uts_loan.uts_loan.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.uts_loan.uts_loan.dto.CustomerDto;
import com.uts_loan.uts_loan.dto.GenericResponse;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.UpdateCustomerDto;
import com.uts_loan.uts_loan.models.Customer;
import com.uts_loan.uts_loan.sevices.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<GenericResponse<Object>> findAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerType) {
    
        Pageable pageable = PageRequest.of(page, size);
    
        PageResponse<CustomerDto> response = customerService.findAll(accountNumber, customerName, customerType, pageable);
    
        return ResponseEntity.ok().body(GenericResponse.builder()
                .success(true)
                .message("Data berhasil diambil")
                .data(response)
                .build());
    }
    
    
        @PostMapping("/create")
        public ResponseEntity<GenericResponse<Customer>> create(@RequestBody CustomerDto dto) {
            Customer newCustomer = customerService.create(dto);
            return ResponseEntity.ok().body(GenericResponse.<Customer>builder()
                    .success(true)
                    .message("Data berhasil ditambahkan")
                    .data(newCustomer)
                    .build());
        }

    

    @PutMapping("/update/{id}")
    public ResponseEntity<GenericResponse<Object>> update(@PathVariable int id ,
                                         @RequestBody UpdateCustomerDto dto){
                                        
        try{
            customerService.update(id, dto);

        }catch(ResponseStatusException ex){
            ex.printStackTrace();
            return ResponseEntity.status(ex.getStatusCode()).body(GenericResponse.builder()
                                .success(true)
                                .message(ex.getReason())
                                .data(null)
                                .build());

        } catch(Exception e){
            return ResponseEntity.internalServerError().body(GenericResponse.builder()
                                .success(true)
                                .message("Terjadi kesalahan di sistem internal")
                                .data(null)
                                .build());
        }

        return ResponseEntity.ok().body(GenericResponse.builder()
                            .success(true)
                            .message("Data berhasil di update")
                            .data(null)
                            .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericResponse<Object>> delete(@PathVariable int id){
        customerService.delete(id);
        return ResponseEntity.ok().body(GenericResponse.builder()
                            .success(true)
                            .message("Data berhasil di delete")
                            .data(null)
                            .build());
    }

   


   
}
