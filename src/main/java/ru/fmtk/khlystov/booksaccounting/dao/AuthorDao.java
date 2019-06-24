package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    int count();

    void insert(Author author);

    Optional<Integer> persist(Author author);

    Optional<Author> findByFullName(String firstName, String secondName);

    Optional<Author> getById(int id);

    Optional<Integer> getId(Author author);

    List<Author> getAll();

    void delete(Author author);
}
