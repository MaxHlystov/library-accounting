package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    List<Genre> findAll();
}
