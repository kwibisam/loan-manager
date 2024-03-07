package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.PaymentHistory;
import com.kwibisam.loanmanager.repository.LoanRepository;
import com.kwibisam.loanmanager.service.PayDateService;
import com.kwibisam.loanmanager.service.PaymentHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentHistoryService paymentHistoryService;
    private final LoanRepository loanRepository;
    private final PayDateService payDateService;

    @PostMapping
    public ResponseEntity<Void> createPayment(
            @RequestHeader("loanId") Long loanId,
            @RequestBody PaymentHistory paymentHistory) {
        PaymentHistory saved = paymentHistoryService.savePayment(loanId,paymentHistory);
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toString();

        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping
    public ResponseEntity<List<PaymentHistory>> getAllPayments() {
        return ResponseEntity.ok(paymentHistoryService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentHistory> getPaymentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentHistoryService.findById(id));
    }
}
