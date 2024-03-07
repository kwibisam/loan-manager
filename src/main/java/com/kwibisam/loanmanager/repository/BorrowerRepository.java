package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    Optional<Borrower> findByPhone(String phone);
    Optional<Borrower> findByNrc(String nrc);
    @Query("SELECT COUNT(DISTINCT b) FROM Borrower b JOIN b.loans l WHERE l.isDisbursed = true")
    Long countActiveBorrowers();
}
