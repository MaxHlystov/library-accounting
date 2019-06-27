package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    int count();

    void insert(Genre genre);

    Genre findByName(String name);

    Optional<Integer> getId(Genre genre);

    Genre getById(int id);

    List<Genre> getAll();

    void update(Genre genre);

    void delete(Genre genre);
}
