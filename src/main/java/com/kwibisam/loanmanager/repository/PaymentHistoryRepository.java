package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    Optional<PaymentHistory> getByTransactionId(String txnId);
}
