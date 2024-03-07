package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    public List<Loan> findByStatus(String status);
    public List<Loan> findByIsDisbursed(Boolean isDisbursed);
}
