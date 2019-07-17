package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

@Component
public class BookRepositoryImpl implements BookRepositoryCustom {
    private MongoTemplate template;

    public BookRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public Book addComment(Book book, Comment comment) {
        book.addComment(comment);
        Book updatedBook = template.save(book);
        return updatedBook;
    }

    @Override
    public List<Comment> findComments(Book book) {
        Book updatedBook = template.save(book);
        return updatedBook.getComments();
    }
}
