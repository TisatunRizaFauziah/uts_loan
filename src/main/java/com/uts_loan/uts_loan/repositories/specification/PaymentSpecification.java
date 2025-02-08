package com.uts_loan.uts_loan.repositories.specification;

import org.springframework.data.jpa.domain.Specification;

import com.uts_loan.uts_loan.models.Payment;

import java.time.LocalDate;

public class PaymentSpecification {

    public static Specification<Payment> filterLoanId(Integer loanId) {
        return (root, query, criteriaBuilder) -> (loanId == null) ? null
                : criteriaBuilder.equal(root.get("loan").get("id"), loanId);
    }

    public static Specification<Payment> filterPaymentDate(LocalDate paymentDate) {
        return (root, query, criteriaBuilder) -> (paymentDate == null) ? null
                : criteriaBuilder.equal(root.get("paymentDate"), paymentDate);
    }

    public static Specification<Payment> filterPaymentMethod(String paymentMethod) {
        return (root, query, criteriaBuilder) -> (paymentMethod == null || paymentMethod.isEmpty()) ? null
                : criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);
    }
}
