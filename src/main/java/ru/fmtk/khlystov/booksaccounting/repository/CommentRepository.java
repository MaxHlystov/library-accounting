package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Comment save(Comment comment);

    List<Comment> findAllByBook(Book book);

    void deleteAllByBook(Book book);
}
