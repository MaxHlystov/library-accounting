package ru.fmtk.khlystov.booksaccounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("ru.fmtk.khlystov.booksaccounting.repository.mongo")
@EnableConfigurationProperties
public class BooksAccountingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksAccountingApplication.class, args);
    }

}
