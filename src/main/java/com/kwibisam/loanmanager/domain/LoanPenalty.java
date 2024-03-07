package com.kwibisam.loanmanager.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
public class LoanPenalty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate date;
    private String reason;
    private Double fee;
    private Double balance;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
}
