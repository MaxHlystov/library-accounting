package ru.fmtk.khlystov.booksaccounting.dao;

import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;

public interface GenreDao {
    int count();

    void insert(Genre genre);

    Genre getById(int id);

    List<Genre> getAll();

}
