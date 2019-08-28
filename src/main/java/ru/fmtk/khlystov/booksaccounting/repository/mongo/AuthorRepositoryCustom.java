package ru.fmtk.khlystov.booksaccounting.repository.mongo;

import ru.fmtk.khlystov.booksaccounting.domain.Author;

public interface AuthorRepositoryCustom {
    Author save(Author author);

    boolean tryDelete(Author genre);
}
