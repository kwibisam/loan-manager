package com.kwibisam.loanmanager;


import com.kwibisam.loanmanager.domain.Loan;
import com.kwibisam.loanmanager.domain.LoanProduct;
import com.kwibisam.loanmanager.domain.PayDate;
import com.kwibisam.loanmanager.repository.BorrowerRepository;
import com.kwibisam.loanmanager.repository.PayDateRepository;
import com.kwibisam.loanmanager.repository.LoanRepository;
import com.kwibisam.loanmanager.repository.LoanProductRepository;
import com.kwibisam.loanmanager.service.PayDateService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.kwibisam.loanmanager.domain.PaymentFrequency.MONTHLY;
import static com.kwibisam.loanmanager.domain.PaymentFrequency.WEEKLY;
import static com.kwibisam.loanmanager.domain.PaymentFrequency.BIWEEKLY;

@Slf4j
@SpringBootApplication
@CrossOrigin(origins = "http://localhost:3000")
public class LoanManagerApplication implements CommandLineRunner {

	private final LoanProductRepository loanProductRepository;
	private final LoanRepository loanRepository;
	private final BorrowerRepository borrowerRepository;
	private final PayDateRepository payDateRepository;
	private final PayDateService payDateService;

	public LoanManagerApplication(LoanProductRepository loanProductRepository, LoanRepository loanRepository, BorrowerRepository borrowerRepository, PayDateRepository payDateRepository, PayDateService payDateService) {
		this.loanProductRepository = loanProductRepository;
		this.loanRepository = loanRepository;
		this.borrowerRepository = borrowerRepository;
		this.payDateRepository = payDateRepository;
		this.payDateService = payDateService;
	}


	public static void main(String[] args) {
		SpringApplication.run(LoanManagerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000","https://lm-admin-two.vercel.app")
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
						.allowedHeaders("*");
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
		LoanProduct monthly = new LoanProduct(MONTHLY, 0.2);
		LoanProduct weekly = new LoanProduct(WEEKLY, 0.1);
		LoanProduct biweekly = new LoanProduct(BIWEEKLY, 0.15);
//		loanProductRepository.saveAll(Arrays.asList(monthly,weekly, biweekly));
		for(LoanProduct loanProduct : loanProductRepository.findAll()) {
			log.info("Type Name: {}, Interest Rate: {}", loanProduct.getFrequency(), loanProduct.getInterestRate());
		}

//		Optional<PayDate> payDate = payDateRepository.findNonClearedPayDateById(102L);
		Optional<PayDate> payDate2 = payDateRepository.findEarliestDueDateByLoanId(102L);
		Optional<PayDate> earliest = payDateRepository.findFirstByLoanIdOrderByDateAsc(102L);

//        payDate.ifPresent(date -> log.info("Non cleared, {}", date));
		if(payDate2.isPresent()) {
			log.info("Earliest Due Date: {}", payDate2.get().getDate());
		}

		if(earliest.isPresent()) {
			log.info("earliest date {}", earliest.get().getDate());
		}
		LocalDate today = LocalDate.now();
		String status = "pending";
		List<PayDate> upcoming = payDateService.findDatesUpcoming(today, today.plusMonths(1));
		List<PayDate> upcomingPending = payDateService.findUpcomingDatesByStatus(today, today.plusMonths(1),status);
		for (PayDate payDate : upcoming) {
			log.info("Upcoming date: {}", payDate.getDate());
			Loan loan = payDate.getLoan();
			log.info("the loan: {}" ,loan.getId());
		}

		log.info("================================NOW THE PENDING ONES================================");
		for (PayDate payDate : upcomingPending) {
			log.info("Upcoming PENDING date: {}", payDate.getDate());
			Loan loan = payDate.getLoan();
			log.info("the loan: {}" ,loan.getId());
		}

		List<Loan> activeLoans = loanRepository.findByIsDisbursedTrue();
		for (Loan activeLoan : activeLoans) {
			Optional<PayDate> optional =
					payDateService.findEarliestDueOrPendingPayDate(activeLoan.getId());
			if(optional.isPresent()) {
				PayDate payDate = optional.get();
				LocalDate end = payDate.getDate();
				LocalDate today2 = LocalDate.now();
				long daysDifference = ChronoUnit.DAYS.between(today2, end);
				log.info("Number of days between today and end date: " + daysDifference);
				activeLoan.setDaysUntilNextPay(daysDifference);
				activeLoan.setNextPayDate(end);
				loanRepository.save(activeLoan);
			}
		}
		log.info("portfolio-size:{}",loanRepository.sumOfDisbursedLoanAmounts());
	}
}
