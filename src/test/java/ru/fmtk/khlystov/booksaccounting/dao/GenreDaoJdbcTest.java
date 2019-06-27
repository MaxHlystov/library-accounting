package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({GenreDaoJdbc.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class GenreDaoJdbcTest {
    private static int count;
    @Autowired
    private GenreDao genreDao;

    @BeforeClass
    public static void initGenreDaoJdbcTest() {
        count = 3;
    }

    @Test
    public void count() {
        int result = genreDao.count();
        assertEquals(count, result);
    }

    @Test
    public void insert() {
        genreDao.insert(new Genre("Test"));
        ++count;
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
        ++count;
        assertThat(id).isGreaterThan(3);
    }

    @Test
    public void findByNameExists() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreDao.findByName(match.getName());
        assertTrue(optGenre.isPresent());
        assertEquals(genreName, optGenre.get().getName());
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
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreDao.getById(1);
        assertTrue(optGenre.isPresent());
        assertEquals(match.getName(), optGenre.get().getName());
    }

    @Test
    public void getByIdNotExisted() {
        Optional<Genre> genre = genreDao.getById(99999);
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getAll() {
        List<Genre> result = genreDao.getAll();
        assertEquals(count, result.size());
    }

    @Test
    public void updateExisted() {
        Genre match = new Genre("ЖЗЛ");
        genreDao.findByName(match.getName()).map(Genre::getId)
                .ifPresent(id -> {
                    String newName = "Пародия";
                    Genre newGenre = new Genre(id, newName);
                    genreDao.update(newGenre);
                    Optional<Genre> optGenre = genreDao.getById(id);
                    assertTrue(optGenre.isPresent());
                    assertEquals(newName, optGenre.get().getName());
                });

    }
}