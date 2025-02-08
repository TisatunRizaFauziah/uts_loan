package com.uts_loan.uts_loan.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import com.uts_loan.uts_loan.models.Loan;

public class LoanSpecification {
    public static Specification<Loan> filterCustomerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> (customerId == null) ? null
                : criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Loan> filterTenor(String tenor) {
        return (root, query, criteriaBuilder) -> (tenor == null || tenor.isEmpty()) ? null
                : criteriaBuilder.like(root.get("tenor"), "%" + tenor + "%");
    }

    public static Specification<Loan> filterStatus(String status) {
        return (root, query, criteriaBuilder) -> (status == null || status.isEmpty()) ? null
                : criteriaBuilder.equal(root.get("status"), status);
    }
}
