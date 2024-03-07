package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}
