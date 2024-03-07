package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Document, Long> {
}
