package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    List<Genre> findAll();
}
