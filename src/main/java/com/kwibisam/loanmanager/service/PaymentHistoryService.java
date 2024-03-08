package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.Loan;
import com.kwibisam.loanmanager.domain.PayDate;
import com.kwibisam.loanmanager.domain.PaymentHistory;
import com.kwibisam.loanmanager.exception.IllegalUpdateException;
import com.kwibisam.loanmanager.exception.ResourceNotFoundException;
import com.kwibisam.loanmanager.repository.LoanRepository;
import com.kwibisam.loanmanager.repository.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final LoanRepository loanRepository;
    private final PayDateService payDateService;
    public PaymentHistory savePayment(Long loanId, PaymentHistory paymentHistory) {
        //get the corresponding loan
        Loan theLoan = loanRepository.findById(loanId)
                        .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        if(theLoan.getLoanBalance() == 0){
            throw new IllegalUpdateException("Loan balance is zero");
        }
        paymentHistory.setLoan(theLoan);
        PaymentHistory saved = paymentHistoryRepository.save(paymentHistory);
        //update earliest due or pending date
        updatePayDate(loanId);

        Optional<PayDate> optional =
                payDateService.findEarliestDueOrPendingPayDate(loanId);
        if(optional.isPresent()) {
            PayDate payDate = optional.get();
            LocalDate end = payDate.getDate();
            LocalDate today2 = LocalDate.now();
            long daysDifference = ChronoUnit.DAYS.between(today2, end);
            log.info("Number of days between today and end date: " + daysDifference);
            theLoan.setDaysUntilNextPay(daysDifference);
            theLoan.setNextPayDate(end);
        }
        //update loan balance
        double newBalance = theLoan.getLoanBalance() - saved.getAmountPaid();
        if(newBalance == 0) {
            theLoan.setStatus("completed");
        }
        theLoan.setLoanBalance(newBalance);
        loanRepository.save(theLoan);
        return saved;
    }

    private void updatePayDate(Long loanId) {
        Optional<PayDate> due = payDateService.findEarliestDuePayDate(loanId);
        PayDate payDate = null;
        if(due.isPresent()) {
            payDate = due.get();
        } else {
            Optional<PayDate> pending = payDateService.findEarliestPendingPayDate(loanId);
            if (pending.isPresent()) {
                payDate = pending.get();
            }
        }

        if (payDate != null) {
            payDate.setStatus("paid");
            payDateService.savePayDate(payDate);
        }

    }

    public void updatePayment(Long id, PaymentHistory patch) {
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if(patch.getAmountPaid() != null) {
            paymentHistory.setAmountPaid(patch.getAmountPaid());
        }
    }

    public List<PaymentHistory> getAllPayments() {
        return paymentHistoryRepository.findAll();
    }

    public PaymentHistory findById(Long id) {
        return paymentHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    public void deletePayment(Long id) {
        Optional<PaymentHistory> optional = paymentHistoryRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("payment not found");
        }
        PaymentHistory payment = optional.get();
        Loan theLoan = loanRepository.findById(payment.getLoan().getId())
                .orElseThrow(RuntimeException::new);
        Double newBalance = theLoan.getLoanBalance() - payment.getAmountPaid();
        theLoan.setLoanBalance(newBalance);
        loanRepository.save(theLoan);
        paymentHistoryRepository.delete(payment);
    }
}
