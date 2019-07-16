package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findAll();
}

