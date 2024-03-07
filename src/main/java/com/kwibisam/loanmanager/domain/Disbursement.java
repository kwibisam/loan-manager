package com.kwibisam.loanmanager.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Disbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date date;
    private Double transactionAmount;
    private String destinationAccount;
    private String mode;
    private String carrier;
    @Column(unique = true)
    private String transactionId;

    @OneToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
