package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.Borrower;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import com.kwibisam.loanmanager.service.BorrowerService;
import com.kwibisam.loanmanager.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/borrowers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final StorageService storageService;
    @PostMapping
    public ResponseEntity<Void> createBorrower(@RequestParam("nrcFile") MultipartFile nrcFile,
                                               @RequestParam("firstName") String firstName,
                                               @RequestParam("lastName") String lastName,
                                               @RequestParam("addressLine1") String addressLine1,
                                               @RequestParam("phone") String phone,
                                               @RequestParam("alternativePhone") String alternativePhone,
                                               @RequestParam("nrc") String nrc,
                                               @RequestParam("dob") String dob,
                                               @RequestParam("income") Double income,
                                               @RequestParam("gender") String gender,
                                               @RequestParam("attachments") List<MultipartFile> attachments) throws IOException {

        // Create Borrower object
        Borrower borrower = new Borrower();
        borrower.setFirstName(firstName);
        borrower.setLastName(lastName);
        borrower.setAddressLine1(addressLine1);
        borrower.setPhone(phone);
        borrower.setAlternativePhone(alternativePhone);
        borrower.setNrc(nrc);
        borrower.setDob(dob);
        borrower.setIncome(String.valueOf(income));
        borrower.setGender(gender);

        // Save borrower
        Borrower saved = borrowerService.saveBorrower(borrower);
        Long id = saved.getId();

        // Handle file upload
        if (!nrcFile.isEmpty()) {
            storageService.saveDocument(nrcFile,"nrc",id);
        }

        // Handle other attachments
        for (MultipartFile attachment : attachments) {
            if (!attachment.isEmpty()) {
                storageService.saveDocument(attachment,"attachment",id);
            }
        }

        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toString();
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping
    public ResponseEntity<List<Borrower>> getBorrowersList(@RequestParam(name = "nrc",required = false) String nrc) {
        if(nrc == null) {
            log.info("NRC  IS NULL");
//            return ResponseEntity.ok(List.of(borrowerService.getBorrowerByNrc(nrc)));
        }

        if(nrc != null) {
            log.info("NRC  IS NOTNULL");
            return ResponseEntity.ok(List.of(borrowerService.getBorrowerByNrc(nrc)));
        }
        return ResponseEntity.ok(borrowerService.getAllBorrowers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrowerById(@PathVariable("id") Long id) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }

}
