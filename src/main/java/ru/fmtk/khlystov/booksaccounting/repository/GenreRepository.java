package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {
    List<Genre> findAll();

    Optional<Genre> findAllByName(String name);
}
