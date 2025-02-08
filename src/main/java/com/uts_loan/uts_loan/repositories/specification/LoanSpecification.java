package com.uts_loan.uts_loan.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import com.uts_loan.uts_loan.models.Loan;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class LoanSpecification {

    public static Specification<Loan> buildSpecification(Integer customerId, String tenor, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(criteriaBuilder.equal(root.get("customer").get("id"), customerId));
            }

            if (tenor != null && !tenor.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("tenor")),
                        "%" + tenor.toLowerCase() + "%"));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
