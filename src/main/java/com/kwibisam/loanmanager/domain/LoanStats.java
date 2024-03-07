package com.kwibisam.loanmanager.domain;

import lombok.Data;

@Data
public class LoanStats {
    private Long totalLoans;
    private Long activeLoans;
    private Long activeBorrowers;
}
