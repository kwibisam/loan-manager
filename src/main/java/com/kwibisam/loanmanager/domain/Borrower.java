package com.kwibisam.loanmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String dob;
    private String addressLine1;
    private String addressLine2;
    @Column(unique = true)
    private String phone;
    private String alternativePhone;
    @Column(unique = true)
    private String nrc;// NRC or Passport number, for ID
    private String income;
    private String gender;

    @JsonIgnore
    @OneToMany(mappedBy = "borrower")
    private List<Document> documents = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();

    public Borrower(String firstName, String lastName,
                    String dob,
                    String addressLine1,
                    String addressLine2,
                    String phone, String alternativePhone, String piNumber, String income) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.phone = phone;
        this.alternativePhone = alternativePhone;
        nrc = piNumber;
        this.income = income;
    }
}
