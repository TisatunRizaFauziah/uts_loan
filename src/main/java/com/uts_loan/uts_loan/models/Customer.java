package com.uts_loan.uts_loan.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="customer")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false, unique = true)
    private int id;

    @Column(name = "account_number", length = 20)
    private String accountNumber;

    @Column(name = "customer_name", length = 500)
    private String customerName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "customer_type", length = 300)
    private String customerType;
    
}
