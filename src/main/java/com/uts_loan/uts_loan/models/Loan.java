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
@Table(name = "Loan")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Loan {
    @Id
    @Column(name = "loan_id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "amount")
    private int amount;

    @Column(name = "remain")
    private int remain;

    @Column(name = "start_date", columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "due_date", columnDefinition = "DATE")
    private LocalDate dueDate;

    @Column(name = "tenor", length = 500)
    private String tenor;

    @Column(name = "status", length = 500)
    private String status;

}
