package ru.fmtk.khlystov.booksaccounting.repository.mongo;

import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;

public class AuthorRepositoryImpl implements AuthorRepositoryCustom {
    private MongoTemplate mongoTemplate;

    public AuthorRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public Author save(Author author) {
        boolean isNewGenre = Strings.isEmpty(author.getId());
        Author newAuthor = mongoTemplate.save(author);
        if (newAuthor != null && !isNewGenre) {
            Query query = Query.query(Criteria.where("author.id").is(new ObjectId(author.getId())));
            Update update = new Update().set("author", author);
            mongoTemplate.updateMulti(query, update, Book.class);
        }
        return newAuthor;
    }

    @Override
    public boolean tryDelete(Author author) {
        Query query = Query.query(Criteria.where("author.id").is(new ObjectId(author.getId())));
        if (mongoTemplate.exists(query, Book.class)) {
            return false;
        }
        mongoTemplate.remove(author);
        return true;
    }
}
