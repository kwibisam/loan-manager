package com.kwibisam.loanmanager.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private PaymentFrequency frequency;
    private Double interestRate;
    private Integer minTerm;
    private String duration;
    public LoanProduct(PaymentFrequency frequency, Double interestRate) {
        this.frequency = frequency;
        this.interestRate = interestRate;
        this.minTerm = 4; //default for all types
        setDuration();
    }

    private void setDuration() {
        switch (frequency){
            case MONTHLY -> {
                this.duration = "Months";
                break;
            }
            case BIWEEKLY -> {
                this.duration = "BiWeeks";
                break;
            }
            case WEEKLY -> {
                this.duration = "Weeks";
                break;
            }
        }
    }

}

