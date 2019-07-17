package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public interface BookRepositoryCustom {

    Book addComment(Book book, Comment comment);

    List<Comment> findComments(Book book);
}
