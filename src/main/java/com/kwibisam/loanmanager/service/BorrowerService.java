package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.Borrower;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowerService {
    private final BorrowerRepository borrowerRepository;
    public Borrower saveBorrower(Borrower borrower) {
        return borrowerRepository.save(borrower);
    }
    public Borrower getBorrowerById(Long id) {
        Optional<Borrower> optional = borrowerRepository.findById(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("borrower not found");
        }
        return optional.get();
    }

    public Borrower getBorrowerByNrc(String nrc) {
        Optional<Borrower> optional = borrowerRepository.findByNrc(nrc);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException("borrower not found");
        }
        return optional.get();
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    public Long countActiveBorrowers() {
        return borrowerRepository.countActiveBorrowers();
    }
}
