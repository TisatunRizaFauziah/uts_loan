package com.uts_loan.uts_loan.sevices;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.uts_loan.uts_loan.dto.CustomerDto;
import com.uts_loan.uts_loan.dto.PageResponse;
import com.uts_loan.uts_loan.dto.UpdateCustomerDto;
import com.uts_loan.uts_loan.models.Customer;

public interface CustomerService {

    PageResponse<CustomerDto> findAll(String accountNumber, String customerName, String customerType,
            Pageable pageable);

    Customer create(CustomerDto dto);

    void update(int id, UpdateCustomerDto dto);

    void delete(int id);
}
