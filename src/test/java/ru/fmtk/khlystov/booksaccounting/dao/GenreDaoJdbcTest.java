package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({GenreDaoJdbc.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class GenreDaoJdbcTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    public void count() {
        int result = genreDao.count();
        assertEquals(3, result);
    }

    @Test
    public void insert() {
        genreDao.insert(new Genre(4, "Test"));
    }

    @Test
    public void getByIdExisted() {
        Genre match = new Genre(1, "This value doesn't matter.");
        Genre genre = genreDao.getById(1);
        assertEquals(match, genre);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getByIdNotExisted() {
        Genre genre = genreDao.getById(99999);
    }

    @Test
    public void getAll() {
        List<Genre> result = genreDao.getAll();
        assertEquals(3, result.size());
    }
}