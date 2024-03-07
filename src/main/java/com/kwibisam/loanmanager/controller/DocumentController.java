package com.kwibisam.loanmanager.controller;

import com.kwibisam.loanmanager.domain.Document;
import com.kwibisam.loanmanager.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final StorageService storageService;

    @PostMapping
    public ResponseEntity<Void> createDoc(
            @RequestHeader("borrowerId") Long borrowerId,
            @RequestParam("file")MultipartFile file,
            @RequestParam("description") String description) {
        try {
            Document saved = storageService.saveDocument(file,description,borrowerId);
            Long id = saved.getId();
            String location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(id)
                    .toString();
            return ResponseEntity.created(URI.create(location)).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(storageService.getDocumentById(id));
    }
}
