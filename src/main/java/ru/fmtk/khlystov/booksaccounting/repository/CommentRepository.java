package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("select comment " +
            "from Comment comment " +
            "   join fetch comment.book " +
            "where comment.book = :book")
    List<Comment> findByBook(@Param("book") Book book);
}
