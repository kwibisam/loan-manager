package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.Borrower;
import com.kwibisam.loanmanager.domain.Loan;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import com.kwibisam.loanmanager.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BorrowerRepository borrowerRepository;

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
}
