package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    long count();

    void insert(Book book);

    Optional<Book> findByTitleAndAuthor(String title, Author author);

    Optional<Book> getById(int id);

    Optional<Integer> getId(Book book);

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre(Genre genre);

    List<Book> getAll();

    boolean update(Book book);

    boolean delete(Book book);
}
