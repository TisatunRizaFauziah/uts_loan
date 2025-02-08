package com.uts_loan.uts_loan.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uts_loan.uts_loan.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
     Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);

     @Query("SELECT c FROM Customer c WHERE " +
               "(:accountNumber IS NULL OR c.accountNumber LIKE CONCAT('%', :accountNumber, '%')) AND " +
               "(:customerName IS NULL OR c.customerName LIKE CONCAT('%', :customerName, '%')) AND " +
               "(:customerType IS NULL OR c.customerType = :customerType)")
     Page<Customer> findByFilters(@Param("accountNumber") String accountNumber,
               @Param("customerName") String customerName,
               @Param("customerType") String customerType,
               Pageable pageable);

}
