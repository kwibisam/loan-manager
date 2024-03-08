package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.Borrower;
import com.kwibisam.loanmanager.domain.Loan;
import com.kwibisam.loanmanager.domain.PayDate;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import com.kwibisam.loanmanager.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BorrowerRepository borrowerRepository;
    private final PayDateService payDateService;

    public Loan saveLoan(Loan loan, Long borrowerId) {
        Optional<Borrower> borrowerOptional = borrowerRepository.findById(borrowerId);
        if(borrowerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Borrower not fount");
        }
        Borrower borrower = borrowerOptional.get();
        loan.setBorrower(borrower);
        return loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateNextPayDate(){
        List<Loan> activeLoans = loanRepository.findByIsDisbursedTrue();
        for (Loan activeLoan : activeLoans) {
            Optional<PayDate> optional =
                    payDateService.findEarliestDueOrPendingPayDate(activeLoan.getId());
            if(optional.isPresent()) {
                PayDate payDate = optional.get();
                LocalDate end = payDate.getDate();
                LocalDate today = LocalDate.now();
                long daysDifference = ChronoUnit.DAYS.between(today, end);
                activeLoan.setDaysUntilNextPay(daysDifference);
                activeLoan.setNextPayDate(end);
                loanRepository.save(activeLoan);
            }
        }
    }
}
