package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.HashMap;
import java.util.List;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Autowired
    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbc = jdbcOperations;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from GENRES",
                new HashMap<>(),
                Integer.class);
    }

    @Override
    public void insert(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        jdbc.update("insert into GENRES (ID, `NAME`) values (:id, :name)",
                params);
    }

    @Override
    public Genre getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from GENRES where ID = :id",
                params,
                new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from GENRES", new GenreMapper());
    }
}
