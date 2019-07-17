package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findByBook(Book book);
}
