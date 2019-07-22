package ru.fmtk.khlystov.booksaccounting.repository;

import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

public class GenreRepositoryImpl implements GenreRepositoryCustom {
    private MongoTemplate mongoTemplate;

    public GenreRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public Genre save(Genre genre) {
        boolean isNewGenre = Strings.isEmpty(genre.getId());
        Genre newGenre = mongoTemplate.save(genre);
        if (newGenre != null && !isNewGenre) {
            Query query = Query.query(Criteria.where("genre.id").is(new ObjectId(genre.getId())));
            Update update = new Update().set("genre.name", genre.getName());
            mongoTemplate.updateMulti(query, update, Book.class);
        }
        return newGenre;
    }

    @Override
    public boolean tryDelete(Genre genre) {
        Query query = Query.query(Criteria.where("genre.id").is(new ObjectId(genre.getId())));
        if (mongoTemplate.exists(query, Book.class)) {
            return false;
        }
        mongoTemplate.remove(genre);
        return true;
    }
}
