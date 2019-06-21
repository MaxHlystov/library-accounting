package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Autowired
    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbcOperations;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from BOOKS",
                new HashMap<>(),
                Integer.class);
    }

    @Override
    public void insert(Book book) {
        HashMap<String, Object> params = new HashMap<>(5);
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        params.put("descr", book.getDescription());
        params.put("authorId", book.getAuthor().getId());
        params.put("genreId", book.getGenre().getId());
        jdbc.update("insert into BOOKS (ID, FIRST_NAME, SECOND_NAME) values (:id, :title, :descr, :authorId, :genreId)",
                params);
    }

    @Override
    public Book getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from BOOKS where ID = :id",
                params,
                this::mapRow);
    }


    @Override
    public List<Book> getByAuthor(Author author) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        return jdbc.query("select * " +
                "from BOOKS " +
                "inner join AUTHORS " +
                "ON BOOKS.AUTHOR_ID = AUTHORS.ID " +
                "AND AUTHORS.FIRST_NAME = :firstName " +
                "AND AUTHORS.SECOND_NAME = :secondName",
                this::mapRow);
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("genreName", genre.getName());
        return jdbc.query("select * " +
                "from BOOKS " +
                "inner join GENRES " +
                "ON BOOKS.GENRE_ID = GENRES.ID " +
                "AND GENRES.NAME = :genreName ",
                this::mapRow);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from BOOKS", this::mapRow);
    }

    private Book mapRow(ResultSet resultSet, int i) throws SQLException {
        int id = resultSet.getInt("ID");
        String title = resultSet.getString("TITLE");
        String descr = resultSet.getString("TITLE");
        int authorId = resultSet.getInt("AUTHOR_ID");
        int genreId = resultSet.getInt("GENRE_ID");
        Author author = authorDao.getById(authorId);
        Genre genre = genreDao.getById(genreId);
        return new Book(id, title, descr, author, genre);
    }
}
