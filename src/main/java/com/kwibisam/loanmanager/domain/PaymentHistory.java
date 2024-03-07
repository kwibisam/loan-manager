package com.kwibisam.loanmanager.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/*
    Represents loan payment records
 */
@Data
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double amountPaid;
    private LocalDate dateOfPayment;
    private String fromAccount;
    private String transactionId;
    private String paymentMode;
    private String paymentCarrier;
    private Boolean isPenaltyPay = false;
    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
}
