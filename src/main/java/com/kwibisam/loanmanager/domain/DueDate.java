package com.kwibisam.loanmanager.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.util.Date;

@Data
@Entity
@Immutable
//@Subselect(value = "SELECT loan_id, start_date + INTERVAL (ROW_NUMBER() OVER (PARTITION BY loan_id ORDER BY start_date) - 1) * 14 AS due_date FROM loans")
//@Subselect(value = "SELECT loan_id, start_date + INTERVAL (ROW_NUMBER() OVER (PARTITION BY loan_id ORDER BY start_date) - 1) * 365 / frequency AS due_date FROM loans")
//@Subselect(value = "SELECT id, DATEADD('DAY', (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1) * 365 / frequency, start_date) AS due_date FROM loan")
//@Subselect(value = "SELECT id, DATEADD('DAY', 28, start_date) AS due_date FROM loan")
//@Subselect(value = "SELECT id, DATEADD('DAY', (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1) * 7, start_date) AS due_date FROM loan")

//@Subselect(value = "SELECT id, CASE WHEN frequency = 7 THEN DATEADD('DAY', (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1) * 7, start_date) WHEN frequency = 14 THEN DATEADD('DAY', (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1) * 14, start_date) WHEN frequency = 30 THEN DATEADD('MONTH', (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1), start_date) END AS due_date FROM loan")
@Subselect(value = "SELECT id, DATE_ADD(start_date, INTERVAL (ROW_NUMBER() OVER (PARTITION BY id ORDER BY start_date) - 1) * 7 DAY) AS due_date FROM loan")

public class DueDate {
    @Id
    private Long id;
    private Date dueDate;
}
