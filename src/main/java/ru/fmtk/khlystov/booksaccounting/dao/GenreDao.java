package ru.fmtk.khlystov.booksaccounting.dao;

import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    int count();

    void insert(Genre genre);

    Optional<Integer> persist(Genre genre);

    Optional<Genre> findByName(String name);

    Optional<Integer> getId(Genre genre);

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    void delete(Genre genre);
}
