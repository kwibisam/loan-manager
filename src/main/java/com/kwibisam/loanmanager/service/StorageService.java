package com.kwibisam.loanmanager.service;

import com.kwibisam.loanmanager.domain.Borrower;
import com.kwibisam.loanmanager.domain.Document;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import com.kwibisam.loanmanager.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final BorrowerRepository borrowerRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;
    public Document saveDocument(MultipartFile file, String desc, Long borrowerId) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            if(!directory.mkdirs()) {
                throw new RuntimeException();
            }
        }

        Optional<Borrower> borrowerOptional = borrowerRepository.findById(borrowerId);
        if(borrowerOptional.isEmpty()) {
            throw new ResourceNotFoundException("borrower not found");
        }

        Borrower borrower = borrowerOptional.get();
        String filePath = uploadDir + File.separator + file.getOriginalFilename();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }

        Document doc = new Document(desc,filePath,borrower);
        return storageRepository.save(doc);
    }

    public Document getDocumentById(Long id) {
        Optional<Document> optional = storageRepository.findById(id);
        if(optional.isEmpty()){
            throw new ResourceNotFoundException("Document not found");
        }
        return optional.get();
    }
}
