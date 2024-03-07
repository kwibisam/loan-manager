package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
    Optional<Disbursement> getByTransactionId(String txnId);
}
