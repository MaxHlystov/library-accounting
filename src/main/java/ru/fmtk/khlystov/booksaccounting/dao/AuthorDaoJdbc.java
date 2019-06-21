package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

//@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbc = jdbcOperations;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from AUTHORS",
                new HashMap<>(),
                Integer.class);
    }

    @Override
    public void insert(Author author) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("id", author.getId());
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        jdbc.update("insert into AUTHORS (ID, FIRST_NAME, SECOND_NAME) values (:id, :firstName, :secondName)",
                params);
    }

    @Override
    public Author getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from AUTHORS where ID = :id",
                params,
                new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from AUTHORS", new AuthorMapper());
    }
}
