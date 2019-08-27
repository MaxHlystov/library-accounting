package ru.fmtk.khlystov.booksaccounting.repository.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Book;

@Component
public class BookRepositoryImpl implements BookRepositoryCustom {
    private MongoTemplate mongoTemplate;
    private CommentRepository commentRepository;

    public BookRepositoryImpl(MongoTemplate mongoTemplate, CommentRepository commentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void delete(Book book) {
        commentRepository.deleteAllByBook(book);
        mongoTemplate.remove(book);
    }
}
