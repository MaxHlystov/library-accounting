package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
        genreDao.insert(new Genre("Test"));
        // Не должно быть исключений.
    }

    @Test
    public void persistExists() {
        int id = genreDao.persist(new Genre("Драма")).orElse(-1);
        assertEquals(1, id);
    }

    @Test
    public void persistNotExists() {
        int id = genreDao.persist(new Genre("Трагедия")).orElse(-1);
        assertThat(id).isGreaterThan(3);
    }

    @Test
    public void findByNameExists() {
        Genre match = new Genre("Драма");
        Optional<Genre> genre = genreDao.findByName(match.getName());
        assertEquals(match, genre.orElse(null));
    }

    @Test
    public void findByNameNotExists() {
        Optional<Genre> genre = genreDao.findByName("&*^%&*^%$^&%$(^&*^&&*^%$");
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getIdExists() {
        Genre match = new Genre("Драма");
        int id = genreDao.getId(match).orElse(-1);
        assertEquals(1, id);
    }

    @Test
    public void getIdNotExists() {
        Genre match = new Genre("&*^%&*^%$^&%$(^&*^&&*^%$");
        int id = genreDao.getId(match).orElse(-1);
        assertEquals(-1, id);
    }

    @Test
    public void getByIdExisted() {
        Genre match = new Genre("Драма");
        Optional<Genre> genre = genreDao.getById(1);
        assertEquals(match, genre.orElse(null));
    }

    @Test
    public void getByIdNotExisted() {
        Optional<Genre> genre = genreDao.getById(99999);
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getAll() {
        List<Genre> result = genreDao.getAll();
        assertEquals(3, result.size());
    }
}