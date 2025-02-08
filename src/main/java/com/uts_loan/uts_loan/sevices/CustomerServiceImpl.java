package com.uts_loan.uts_loan.sevices;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uts_loan.uts_loan.dto.CustomerDto;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.UpdateCustomerDto;
import com.uts_loan.uts_loan.models.Customer;
import com.uts_loan.uts_loan.models.Loan;
import com.uts_loan.uts_loan.models.Payment;
import com.uts_loan.uts_loan.repositories.CustomerRepository;
import com.uts_loan.uts_loan.repositories.LoanRepository;
import com.uts_loan.uts_loan.repositories.PaymentRepository;
import com.uts_loan.uts_loan.repositories.specification.CustomerSpecification;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository,
            LoanRepository loanRepository,
            PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
    }

    public CustomerDto mapToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .accountNumber(customer.getAccountNumber())
                .customerName(customer.getCustomerName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .customerType(customer.getCustomerType())
                .build();
    }

    @Override
    public Customer create(CustomerDto dto) {
        Customer customer = mapToCustomerDto(dto);
        return customerRepository.save(customer);

    }

    // create
    public Customer mapToCustomerDto(CustomerDto dto) {
        return Customer.builder()
                .id(dto.getId())
                .accountNumber(dto.getAccountNumber())
                .customerName(dto.getCustomerName())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .customerType(dto.getCustomerType())
                .build();
    }

    @Override
    public void delete(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer tidak ditemukan");
        }

        List<Loan> loans = loanRepository.findByCustomerId(id);

        for (Loan loan : loans) {
            List<Payment> payments = paymentRepository.findByLoanId(loan.getId());
            if (!payments.isEmpty()) {
                paymentRepository.deleteAll(payments);
            }
            loanRepository.delete(loan);
        }
        customerRepository.delete(customer.get());
    }

    @Override
    public void update(int id, UpdateCustomerDto dto) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {

            Customer customerToUpdate = customer.get();
            customerToUpdate.setAccountNumber(dto.getAccountNumber());
            customerToUpdate.setCustomerName(dto.getCustomerName());
            customerToUpdate.setPhoneNumber(dto.getPhoneNumber());
            customerToUpdate.setAddress(dto.getAddress());
            customerToUpdate.setCustomerType(dto.getCustomerType());
            customerRepository.save(customerToUpdate);

        } else {
            log.debug("Sample id yang di cari : {}", id); // ini muncul ketika di level nya di ubah ke level debug
            log.info("Sample id yang di cari : {}", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, " sample id tidak ditemukan");
        }

    }
    
    @Override
    public PageResponse<CustomerDto> findAll(String accountNumber, String customerName, String customerType,
            Pageable pageable) {
        Specification<Customer> spec = Specification.where(null);

        if (accountNumber != null && !accountNumber.isEmpty()) {
            spec = spec.and(CustomerSpecification.containAccountNumber(accountNumber));
        }
        if (customerName != null && !customerName.isEmpty()) {
            spec = spec.and(CustomerSpecification.containName(customerName));
        }
        if (customerType != null && !customerType.isEmpty()) {
            spec = spec.and(CustomerSpecification.filterByCustomerType(customerType));
        }

        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);

        // Cek apakah hasil query kosong
        if (customerPage.isEmpty()) {
            System.out.println("No customers found.");
            return PageResponse.<CustomerDto>builder()
                    .page(pageable.getPageNumber())
                    .size(pageable.getPageSize())
                    .totalPage(0)
                    .totalItem(0)
                    .items(Collections.emptyList())
                    .build();
        }

        // Mapping ke DTO
        List<CustomerDto> customerDtos = customerPage.getContent().stream()
                .map(this::mapToCustomerDto)
                .toList();

        // Cek hasil mapping
        if (customerDtos.isEmpty()) {
            System.out.println("Mapping to CustomerDto failed.");
        }

        return PageResponse.<CustomerDto>builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalPage(customerPage.getTotalPages())
                .totalItem(customerPage.getTotalElements())
                .items(customerDtos)
                .build();
    }

}
