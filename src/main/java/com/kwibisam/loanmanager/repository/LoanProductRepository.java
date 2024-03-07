package com.kwibisam.loanmanager.repository;

import com.kwibisam.loanmanager.domain.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {
}
