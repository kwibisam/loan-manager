package com.kwibisam.loanmanager.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/*
    Represents Loan repayment schedules and details
 */
@Data
@Entity
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /*
        Scheduled payment details
     */
    private Date paymentDate; //date the payment should be made on
    private Double expectedPayment;
    private String paymentStatus;// Due, Paid, Pending
    private Boolean isNextPayment;  //help give reminders when next payment date is approaching.
    private Boolean isPaymentDatePassed;
    private Boolean isExtension;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

}
