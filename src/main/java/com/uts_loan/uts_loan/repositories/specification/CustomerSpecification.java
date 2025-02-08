package com.uts_loan.uts_loan.repositories.specification;

import org.springframework.data.jpa.domain.Specification;
import com.uts_loan.uts_loan.models.Customer;

public class CustomerSpecification {
    public static Specification<Customer> containAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("accountNumber"),
                "%" + accountNumber + "%");
    }

    public static Specification<Customer> containName(String customerName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("customerName"),
                "%" + customerName + "%");
    }

    public static Specification<Customer> filterByCustomerType(String customerType) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerType"), customerType);
    }
}
