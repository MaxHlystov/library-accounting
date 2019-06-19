package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("id");
        String firstName = resultSet.getString("firstName");
        String secondName = resultSet.getString("secondName");
        return new Author(id, firstName, secondName);
    }

}
