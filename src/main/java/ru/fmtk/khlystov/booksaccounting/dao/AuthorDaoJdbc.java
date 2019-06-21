package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        jdbc.update("INSERT INTO AUTHORS " +
                        "   (FIRST_NAME, SECOND_NAME) " +
                        "VALUES (:firstName, :secondName) " +
                        "WHERE NOT EXISTS " +
                        "   (SELECT 1 FROM AUTHORS " +
                        "       WHERE FIRST_NAME = :firstName " +
                        "           AND SECOND_NAME = :secondName)",
                params);
    }

    @Override
    @Transactional
    public Optional<Integer> persist(Author author) {
        insert(author);
        return getId(author);
    }

    @Override
    public Optional<Author> findByFullName(String firstName, String secondName) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("firstName", firstName);
        params.put("secondName", secondName);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * " +
                                    "FROM AUTHORS " +
                                    "WHERE FIRST_NAME = :firstName AND SECOND_NAME = :secondName " +
                                    "LIMIT 1",
                            params,
                            new AuthorMapper()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getId(Author author) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT ID " +
                                    "FROM AUTHORS " +
                                    "WHERE FIRST_NAME = :firstName AND SECOND_NAME = :secondName " +
                                    "LIMIT 1",
                            params,
                            (resultSet, i) -> resultSet.getInt("ID")));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * " +
                                    "FROM AUTHORS " +
                                    "WHERE ID = :id " +
                                    "LIMIT 1",
                            params,
                            new AuthorMapper()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("SELECT * FROM AUTHORS", new AuthorMapper());
    }
}