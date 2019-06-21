package ru.fmtk.khlystov.booksaccounting.dao;

import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;

public interface BookDao {
    int count();

    void insert(Book book);

    Book getById(int id);

    List<Book> getByAuthor(Author author);

    List<Book> getByGenre(Genre genre);

    List<Book> getAll();
}
