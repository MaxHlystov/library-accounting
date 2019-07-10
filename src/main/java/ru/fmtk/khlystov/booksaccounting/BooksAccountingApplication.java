package ru.fmtk.khlystov.booksaccounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@SpringBootApplication
@EnableMapRepositories("ru.fmtk.khlystov.booksaccounting.repository")
public class BooksAccountingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksAccountingApplication.class, args);
	}

}
