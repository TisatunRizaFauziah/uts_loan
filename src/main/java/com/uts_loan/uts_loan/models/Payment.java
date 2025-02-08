package com.uts_loan.uts_loan.models;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payment")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "loan_id", referencedColumnName = "loan_id")
    Loan loan;

    @Column(name = "amount")
    private int amount;

    @Column(name = "payment_date", columnDefinition = "DATE")
    private LocalDate paymentDate;

    @Column(name = "payment_method", length = 500)
    private String paymentMethod;

}
