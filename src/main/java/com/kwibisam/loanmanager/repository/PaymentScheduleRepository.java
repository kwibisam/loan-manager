package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    Optional<PaymentSchedule> getByPaymentDate(Date date);

//    List<PaymentSchedule> findByPaymentStatusOrderByPaymentDateAsc(String status);

//    @Query("SELECT s from PaymentSchedule s WHERE s.loan_id =?1")
//    List<PaymentSchedule> findByLoan(Long id);
}
