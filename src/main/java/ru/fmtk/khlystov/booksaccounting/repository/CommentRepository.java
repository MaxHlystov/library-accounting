package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> getForBook(Book book);

    void addComment(Comment comment);

}
