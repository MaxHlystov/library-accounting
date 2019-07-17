package ru.fmtk.khlystov.booksaccounting.repository;

import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final BookRepository bookRepository;

    public CommentRepositoryImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Comment> findByBook(Book book) {
        Book updatedBook = bookRepository.save(book);
        return updatedBook.getComments();
    }
}
