package com.kwibisam.loanmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double loanAmount;
    private Double interestAmount;  //calculated from principal and interest rate
    private Double interestRate;    //derived from loan product
    private Integer duration;   //derived from type[weekly, bi-weekly...] and loan amount(principal)
    @Enumerated(EnumType.STRING)
    private PaymentFrequency frequency;
    private Double repaymentAmount;
    private String purpose;
    @Column(name = "start_date")
    private LocalDate startDate;
    private String status = "SUBMITTED";
    private Double loanBalance;
    private Double totalPayOff;
    private Boolean isDisbursed = false;

    @CreationTimestamp
    private LocalDate createdAt = LocalDate.now();
    @UpdateTimestamp
    private LocalDate updatedAt = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "loan")
    private List<LoanPenalty> penalties = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "loan")
    private Disbursement disbursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id", nullable = false)
    private Borrower borrower;
    @JsonIgnore
    @OneToMany(mappedBy = "loan")
    private List<PaymentHistory> paymentHistory;

    @JsonIgnore
    @OneToMany(mappedBy = "loan")
    private List<PayDate> payDates;
}
