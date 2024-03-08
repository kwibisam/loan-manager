package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.LoanStats;
import com.kwibisam.loanmanager.repository.LoanRepository;
import com.kwibisam.loanmanager.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app/stats")
@RequiredArgsConstructor
public class StatsController {
    private final BorrowerService borrowerService;
    private final LoanRepository loanRepository;

    @GetMapping
    public ResponseEntity<LoanStats> getStats() {
        long activeBorrowers = borrowerService.countActiveBorrowers();
        long activeLoans = loanRepository.countByIsDisbursedTrue();
        long allLoans = loanRepository.countAllLoans();
        double portSize = loanRepository.sumOfDisbursedLoanAmounts();
        LoanStats stats = new LoanStats();
        stats.setTotalLoans(allLoans);
        stats.setActiveLoans(activeLoans);
        stats.setActiveBorrowers(activeBorrowers);
        stats.setPortfolioSize(portSize);
        return ResponseEntity.ok(stats);
    }
}
