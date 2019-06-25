package ru.fmtk.khlystov.booksaccounting.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbcOperations;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public int count() {
        Integer res = jdbc.queryForObject("SELECT count(*) FROM BOOKS",
                new HashMap<>(),
                Integer.class);
        return Objects.requireNonNullElse(res, -1);
    }

    @Override
    @Transactional
    public void insert(Book book) {
        Author author = book.getAuthor();
        Optional<Integer> optionalAuthorId = authorDao.persist(author);
        if (optionalAuthorId.isEmpty()) {
            return;
        }
        Genre genre = book.getGenre();
        Optional<Integer> optionalGenreId = genreDao.persist(genre);
        if (optionalGenreId.isEmpty()) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>(4);
        params.put("title", book.getTitle());
        params.put("descr", book.getDescription());
        params.put("authorId", optionalAuthorId.get());
        params.put("genreId", optionalGenreId.get());
        jdbc.update("INSERT INTO BOOKS " +
                        "   (TITLE, DESCRIPTION, AUTHOR_ID, GENRE_ID) " +
                        "SELECT :title, :descr, :authorId, :genreId " +
                        "WHERE NOT EXISTS " +
                        "   (SELECT * FROM BOOKS " +
                        "       WHERE TITLE=:title " +
                        "           AND AUTHOR_ID=:authorId)",
                params);
    }

    @Override
    @Transactional
    public Optional<Integer> persist(Book book) {
        insert(book);
        return getId(book);
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, Author author) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("title", title);
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT BOOKS.ID " +
                                    "FROM BOOKS " +
                                    "   INNER JOIN AUTHORS " +
                                    "       ON BOOKS.AUTHOR_ID = AUTHORS.ID " +
                                    "           AND AUTHORS.FIRST_NAME = :firstName " +
                                    "          AND AUTHORS.SECOND_NAME = :secondName " +
                                    "           AND BOOKS.TITLE = :title " +
                                    "LIMIT 1",
                            params,
                            this::mapRow));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> getId(Book book) {
        Author author = book.getAuthor();
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("title", book.getTitle());
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT BOOKS.ID " +
                                    "FROM BOOKS " +
                                    "INNER JOIN AUTHORS " +
                                    "   ON BOOKS.AUTHOR_ID = AUTHORS.ID " +
                                    "       AND AUTHORS.FIRST_NAME = :firstName " +
                                    "       AND AUTHORS.SECOND_NAME = :secondName " +
                                    "       AND BOOKS.TITLE = :title ",
                            params,
                            (resultSet, i) -> resultSet.getInt("ID")));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> getById(int id) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject("SELECT * " +
                                    "FROM BOOKS " +
                                    "WHERE ID = :id " +
                                    "LIMIT 1",
                            params,
                            this::mapRow));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }


    @Override
    public List<Book> getByAuthor(Author author) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("firstName", author.getFirstName());
        params.put("secondName", author.getSecondName());
        return jdbc.query("SELECT * " +
                        "FROM BOOKS " +
                        "INNER JOIN AUTHORS " +
                        "ON BOOKS.AUTHOR_ID = AUTHORS.ID " +
                        "AND AUTHORS.FIRST_NAME = :firstName " +
                        "AND AUTHORS.SECOND_NAME = :secondName",
                params,
                this::mapRow);
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("genreName", genre.getName());
        return jdbc.query("SELECT * " +
                        "FROM BOOKS " +
                        "INNER JOIN GENRES " +
                        "   ON BOOKS.GENRE_ID = GENRES.ID " +
                        "       AND GENRES.NAME = :genreName ",
                params,
                this::mapRow);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("SELECT * FROM BOOKS", this::mapRow);
    }

    private Book mapRow(ResultSet resultSet, int i) throws SQLException {
        String title = resultSet.getString("TITLE");
        String descr = resultSet.getString("TITLE");
        int authorId = resultSet.getInt("AUTHOR_ID");
        int genreId = resultSet.getInt("GENRE_ID");
        Optional<Author> optAuthor = authorDao.getById(authorId);
        Optional<Genre> optGenre = genreDao.getById(genreId);
        return new Book(title, descr,
                optAuthor.orElse(new Author("Автор", "Неизвестен")),
                optGenre.orElse(new Genre("Жанр неопределен")));
    }

    @Override
    @Transactional
    public void delete(Book book) {
        getId(book).ifPresent(id -> {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("id", id);
            jdbc.update("DELETE FROM BOOKS WHERE ID = (:id)",
                    params);
        });
    }
}
