package com.uts_loan.uts_loan.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uts_loan.uts_loan.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
     Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);

     Optional<Customer> findByAccountNumber(String accountNumber);
}
