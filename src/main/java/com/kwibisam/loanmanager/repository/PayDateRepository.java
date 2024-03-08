package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.PayDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayDateRepository extends JpaRepository<PayDate, Long> {
    Optional<PayDate> findFirstByLoanIdOrderByDateAsc(Long id);
    List<PayDate> findByDate(LocalDate date);
    @Query("SELECT pd FROM PayDate pd WHERE pd.loan.id = :loanId AND pd.status = 'due' ORDER BY pd.date ASC LIMIT 1")
    Optional<PayDate> findEarliestDueDateByLoanId(@Param("loanId") Long loanId);

    @Query("SELECT pd FROM PayDate pd WHERE pd.loan.id = :loanId AND (pd.status = 'due' OR pd.status = 'pending') ORDER BY pd.date ASC LIMIT 1")
    Optional<PayDate> findEarliestDueOrPendingDateByLoanId(@Param("loanId") Long loanId);


    @Query("SELECT pd FROM PayDate pd WHERE pd.loan.id = :loanId AND pd.status = 'pending' ORDER BY pd.date ASC LIMIT 1")
    Optional<PayDate> findEarliestPendingDateByLoanId(@Param("loanId") Long loanId);

    void deleteAllByLoanId(Long loanId);
    List<PayDate> findByDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT pd FROM PayDate pd WHERE pd.date BETWEEN :startDate AND :endDate AND pd.status = :status")
    List<PayDate> findDatesBetweenByStatus(LocalDate startDate, LocalDate endDate, String status);

}
