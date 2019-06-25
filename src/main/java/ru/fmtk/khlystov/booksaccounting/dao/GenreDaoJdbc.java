package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbc = jdbcOperations;
    }

    @Override
    public int count() {
        Integer res = jdbc.queryForObject("SELECT count(*) FROM GENRES",
                new HashMap<>(),
                Integer.class);
        return Objects.requireNonNullElse(res, -1);
    }

    @Override
    public void insert(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", genre.getName());
        jdbc.update("INSERT INTO GENRES (`NAME`) " +
                        "SELECT :name " +
                        "WHERE NOT EXISTS " +
                        "(SELECT * FROM GENRES WHERE `NAME`=:name)",
                params);
    }

    @Override
    @Transactional
    public Optional<Integer> persist(Genre genre) {
        insert(genre);
        return getId(genre);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", name);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * " +
                                    "FROM GENRES " +
                                    "WHERE `NAME` = :name " +
                                    "LIMIT 1",
                            params,
                            new GenreMapper()));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getId(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", genre.getName());
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT ID " +
                                    "FROM GENRES " +
                                    "WHERE `NAME` = :name " +
                                    "LIMIT 1",
                            params,
                            (resultSet, i) -> resultSet.getInt("ID")));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * " +
                                    "FROM GENRES " +
                                    "WHERE ID = :id " +
                                    "LIMIT 1",
                            params,
                            new GenreMapper()));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("SELECT * FROM GENRES", new GenreMapper());
    }

    @Override
    public int update(Genre oldGenre, Genre newGenre) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("name", oldGenre.getName());
        params.put("newName", newGenre.getName());
        return jdbc.update("UPDATE GENRES " +
                        "SET `NAME` = :newName " +
                        "WHERE `NAME` = :name",
                params);
    }

    @Override
    public void delete(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", genre.getName());
        jdbc.update("DELETE FROM GENRES WHERE `NAME` = (:name)",
                params);
    }
}
