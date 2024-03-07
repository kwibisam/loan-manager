package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.*;
import com.kwibisam.loanmanager.repository.DisbursementRepository;
import com.kwibisam.loanmanager.repository.PayDateRepository;
import com.kwibisam.loanmanager.repository.LoanRepository;
import com.kwibisam.loanmanager.service.BorrowerService;
import com.kwibisam.loanmanager.service.PayDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanRepository loanRepository;
    private final BorrowerService borrowerService;
    private final DisbursementRepository disbursementRepository;
//    private final PayDateRepository payDateRepository;
    private final PayDateService payDateService;

    @GetMapping()
    public Iterable<Loan> getLoans(
            @RequestParam(required = false, name = "active") Boolean active,
            @RequestParam(required = false, name = "status") String status) {
        if(active != null) {
            return loanRepository.findByIsDisbursed(true);
        } else if (status != null) {
            return loanRepository.findByStatus(status);
        } else {
            return loanRepository.findAll();
        }
    }



    @PostMapping()
    public ResponseEntity<Void> createLoan(
            @RequestBody Loan loan,
            @RequestHeader("borrowerId") Long borrowerId) {
        Borrower borrower = borrowerService.getBorrowerById(borrowerId);
        loan.setBorrower(borrower);
        Loan saved = loanRepository.save(loan);
        Long id = saved.getId();
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toString();
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") Long id) {
        Optional<Loan> loan = loanRepository.findById(id);
        if (loan.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return ResponseEntity.ok(loan.get());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateLoan(@PathVariable("id") Long id, @RequestBody Loan patch) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(patch.getStatus() != null) {
            existingLoan.setStatus(patch.getStatus());
        }
        loanRepository.save(existingLoan);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteLoan(@PathVariable("id") Long id) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        loanRepository.delete(existingLoan);
        return ResponseEntity.noContent().build();
    }

//        DISBURSEMENT                 //

    @PostMapping("/{id}/disbursement")
    public ResponseEntity<Void> createLoanDisbursement(
            @PathVariable("id") Long id,
            @RequestBody Disbursement disbursement) {
        Optional<Loan> optional = loanRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Loan not found");
        }
        Loan theLoan = optional.get();
        disbursement.setLoan(theLoan);
        disbursementRepository.save(disbursement);
        theLoan.setIsDisbursed(true);
        theLoan.setLoanBalance(theLoan.getTotalPayOff());
        theLoan.setStartDate(LocalDate.now());
        //generate pay schedule
        List<PayDate> payDateList = getPayDates(theLoan);
        loanRepository.save(theLoan);
        payDateService.saveAll(payDateList);
       return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/disbursement")
    public ResponseEntity<Void> deleteLoanDisbursement(
            @PathVariable("id") Long id) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        Disbursement disbursement = existingLoan.getDisbursement();
        disbursementRepository.delete(disbursement);
        payDateService.deleteAllLoanPayDates(id);
        loanRepository.save(existingLoan);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disbursement")
    public ResponseEntity<Disbursement> updateLoanDisbursement(
            @PathVariable("id") Long id, Disbursement patch) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        Disbursement disbursement = existingLoan.getDisbursement();

        if(patch.getCarrier() != null) {
            disbursement.setCarrier(patch.getCarrier());
        }
        if(patch.getDate() != null) {
            disbursement.setDate(patch.getDate());
        }
        if(patch.getTransactionId() != null) {
            disbursement.setTransactionId(patch.getTransactionId());
        }
        if(patch.getTransactionAmount() != null) {
            disbursement.setTransactionAmount(patch.getTransactionAmount());
        }
        if(patch.getDestinationAccount() != null) {
            disbursement.setDestinationAccount(patch.getDestinationAccount());
        }
        disbursementRepository.save(disbursement);
        return ResponseEntity.ok(disbursement);
    }

    @GetMapping("/{id}/disbursement")
    public ResponseEntity<Disbursement> getLoanDisbursement(
            @PathVariable("id") Long id) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        Disbursement disbursement = existingLoan.getDisbursement();
        return ResponseEntity.ok(disbursement);
    }

    /*
        PAY DATES
     */
    @GetMapping("/{id}/payment-dates")
    public ResponseEntity<List<PayDate>> getPayDates(@PathVariable("id") Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        return ResponseEntity.ok(loan.getPayDates());
    }


    private static List<PayDate> getPayDates(Loan theLoan) {
        int numberOfDueDates = theLoan.getDuration();
        List<LocalDate> localDates = new ArrayList<>();
        List<PayDate> payDateList = new ArrayList<>();
        LocalDate start = theLoan.getStartDate();

        for(int i =0; i < numberOfDueDates; i++) {
            if(theLoan.getFrequency().equals(PaymentFrequency.MONTHLY)){
                start = start.plusMonths(1);
            } else if (theLoan.getFrequency().equals(PaymentFrequency.WEEKLY)) {
                start = start.plusWeeks(1);
            } else {
                start = start.plusWeeks(2);
            }

            localDates.add(start);
        }

        for (LocalDate localDate : localDates) {
            PayDate payDate = new PayDate();
            payDate.setLoan(theLoan);
            payDate.setDate(localDate);
            payDateList.add(payDate);
        }
        return payDateList;
    }

}
