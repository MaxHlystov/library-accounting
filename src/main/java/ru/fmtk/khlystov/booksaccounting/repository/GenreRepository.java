package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    long count();

    void insert(Genre genre);

    Optional<Genre> findByName(String name);

    Optional<Integer> getId(Genre genre);

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    boolean update(Genre genre);

    boolean delete(Genre genre);
}
