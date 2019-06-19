package ru.fmtk.khlystov.booksaccounting.dao;

import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();

    void insert(Author author);

    Author getById(int id);

    List<Author> getAll();
}
