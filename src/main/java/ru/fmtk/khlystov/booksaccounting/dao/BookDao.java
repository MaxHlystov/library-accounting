package ru.fmtk.khlystov.booksaccounting.dao;

import ru.fmtk.khlystov.booksaccounting.domain.Book;

import java.util.List;

public interface BookDao {
    int count();

    void insert(Book book);

    Book getById(int id);

    List<Book> getByAuthor();

    List<Book> getByGenre();

    List<Book> getAll();
}
