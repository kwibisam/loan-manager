package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.PayDate;
import com.kwibisam.loanmanager.repository.PayDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayDateService {
    private final PayDateRepository payDateRepository;

    public Optional<PayDate> findEarliestDuePayDate(Long loanId) {
        return payDateRepository.findEarliestDueDateByLoanId(loanId);
    }

    public Optional<PayDate> findEarliestDueOrPendingPayDate(Long loanId) {
        return payDateRepository.findEarliestDueOrPendingDateByLoanId(loanId);
    }


    public Optional<PayDate> findEarliestPendingPayDate(Long loanId) {
        return payDateRepository.findEarliestPendingDateByLoanId(loanId);
    }

    public void deleteAllLoanPayDates(Long loanId) {
        payDateRepository.deleteAllByLoanId(loanId);
    }

    public void savePayDate(PayDate payDate) {
        payDateRepository.save(payDate);
    }

    public void saveAll(List<PayDate> payDateList) {
        payDateRepository.saveAll(payDateList);
    }
    public List<PayDate> findDatesUpcoming(LocalDate starDate, LocalDate endDate) {
        return payDateRepository.findByDateBetween(starDate,endDate);
    }

    public List<PayDate> findUpcomingDatesByStatus(LocalDate starDate, LocalDate endDate, String status) {
        return payDateRepository.findDatesBetweenByStatus(starDate,endDate,status);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePaymentStatuses() {
        LocalDate today = LocalDate.now();
        List<PayDate> payDates = payDateRepository.findByDate(today);
        for (PayDate payDate : payDates) {
            if(payDate.getStatus().equals("pending")) {
                payDate.setStatus("due");
                payDateRepository.save(payDate);
            }
        }
    }
}
