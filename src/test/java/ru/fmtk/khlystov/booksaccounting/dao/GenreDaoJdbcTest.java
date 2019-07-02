package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;
import ru.fmtk.khlystov.booksaccounting.repository.GenreRepositoryJpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({GenreRepositoryJpa.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class GenreDaoJdbcTest {
    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @Test
    public void count() {
        long result = genreRepositoryJpa.count();
        assertEquals(3L, result);
    }

    @Test
    public void insert() {
        genreRepositoryJpa.insert(new Genre("Test"));
        // Не должно быть исключений.
    }

    @Test
    public void findByNameExists() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreRepositoryJpa.findByName(match.getName());
        assertTrue(optGenre.isPresent());
        assertEquals(genreName, optGenre.get().getName());
    }

    @Test
    public void findByNameNotExists() {
        Optional<Genre> genre = genreRepositoryJpa.findByName("&*^%&*^%$^&%$(^&*^&&*^%$");
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getIdExists() {
        Genre match = new Genre("Драма");
        int id = genreRepositoryJpa.getId(match).orElse(-1);
        assertEquals(1, id);
    }

    @Test
    public void getIdNotExists() {
        Genre match = new Genre("&*^%&*^%$^&%$(^&*^&&*^%$");
        int id = genreRepositoryJpa.getId(match).orElse(-1);
        assertEquals(-1, id);
    }

    @Test
    public void getByIdExisted() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreRepositoryJpa.getById(1);
        assertTrue(optGenre.isPresent());
        assertEquals(match.getName(), optGenre.get().getName());
    }

    @Test
    public void getByIdNotExisted() {
        Optional<Genre> genre = genreRepositoryJpa.getById(99999);
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getAll() {
        List<Genre> result = genreRepositoryJpa.getAll();
        assertEquals(3, result.size());
    }

    @Test
    public void updateExisted() {
        Genre match = new Genre("ЖЗЛ");
        genreRepositoryJpa.findByName(match.getName()).map(Genre::getId)
                .ifPresent(id -> {
                    String newName = "Пародия";
                    genreRepositoryJpa.update(new Genre(id, newName));
                    Optional<Genre> optGenre = genreRepositoryJpa.getById(id);
                    assertTrue(optGenre.isPresent());
                    assertEquals(newName, optGenre.get().getName());
                });

    }
}