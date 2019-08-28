package ru.fmtk.khlystov.booksaccounting.repository.mongo;

import ru.fmtk.khlystov.booksaccounting.domain.Genre;

public interface GenreRepositoryCustom {
    Genre save(Genre genre);

    boolean tryDelete(Genre genre);
}
