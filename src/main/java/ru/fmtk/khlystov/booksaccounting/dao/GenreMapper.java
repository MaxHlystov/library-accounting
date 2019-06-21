package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        String name = resultSet.getString("NAME");
        return new Genre(name);
    }
}
