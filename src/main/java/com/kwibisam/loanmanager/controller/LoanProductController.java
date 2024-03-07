package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.LoanProduct;
import com.kwibisam.loanmanager.repository.LoanProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/loan-products")
@RequiredArgsConstructor
public class LoanProductController {

    private final LoanProductRepository repository;


    @GetMapping
    public ResponseEntity<List<LoanProduct>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createLoanProduct(@RequestBody LoanProduct loanProduct) {
        LoanProduct saved = repository.save(loanProduct);
        Long id = saved.getId();
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toString();
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanProduct> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanProduct> updateLoanProduct(@PathVariable Long id, @RequestBody LoanProduct loanProduct) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        loanProduct.setId(id); // Ensure ID is set for update
        LoanProduct updated = repository.save(loanProduct);
        return ResponseEntity.ok(updated);
    }
}
