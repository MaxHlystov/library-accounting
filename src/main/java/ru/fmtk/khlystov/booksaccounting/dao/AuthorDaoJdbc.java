package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

//@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbc = jdbcOperations;
    }

    @Override
    public int count() {
        Integer res = jdbc.queryForObject("select count(*) from AUTHORS",
                new HashMap<>(),
                Integer.class);
        return Objects.requireNonNullElse(res, -1);
    }

    @Override
    public void insert(Author author) {
        if (author.getId() >= 0) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        jdbc.update("INSERT INTO AUTHORS " +
                        "   (FIRST_NAME, SECOND_NAME) " +
                        "SELECT :firstName, :secondName " +
                        "WHERE NOT EXISTS " +
                        "   (SELECT * FROM AUTHORS WHERE " +
                        "       FIRST_NAME = :firstName " +
                        "      AND SECOND_NAME = :secondName)",
                params);
    }

    @Override
    @Transactional
    public Optional<Integer> persist(Author author) {
        if (author.getId() < 0) {
            insert(author);
        }
        Optional<Integer> optId = getId(author);
        optId.ifPresent(id -> author.setId(id));
        return optId;
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
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getId(Author author) {
        int id = author.getId();
        if (id >= 0) {
            return Optional.ofNullable(id);
        }
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
        } catch (DataAccessException ex) {
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
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("SELECT * FROM AUTHORS", new AuthorMapper());
    }

    @Override
    public int update(Author author) {
        int id = author.getId();
        if (id < 0) {
            return 0;
        }
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("id", author.getId());
        params.put("newFirstName", author.getFirstName());
        params.put("newSecondName", author.getSecondName());
        return jdbc.update("UPDATE AUTHORS " +
                        "SET FIRST_NAME = :newFirstName, SECOND_NAME = :newSecondName " +
                        "WHERE ID = :id",
                params);
    }

    @Override
    @Transactional
    public void delete(Author author) {
        int id = author.getId();
        if (id < 0) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        jdbc.update("DELETE FROM AUTHORS WHERE ID = (:id)", params);
    }
}