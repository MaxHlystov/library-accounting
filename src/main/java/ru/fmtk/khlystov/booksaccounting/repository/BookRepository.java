package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {

    Optional<Book> findByTitleAndAuthor(String title, Author author);

    List<Book> findAllByAuthor(Author author);

    List<Book> findAllByGenre(Genre genre);

    List<Book> findAll();
}
