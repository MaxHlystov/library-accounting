package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;
import ru.fmtk.khlystov.booksaccounting.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class GenreDaoJdbcTest {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void count() {
        long result = genreRepository.count();
        assertEquals(3L, result);
    }

    @Test
    public void save() {
        genreRepository.save(new Genre("Test"));
        // Не должно быть исключений.
    }

    @Test
    public void findAllByNameExists() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreRepository.findAllByName(match.getName());
        assertTrue(optGenre.isPresent());
        assertEquals(genreName, optGenre.get().getName());
    }

    @Test
    public void findAllByNameNotExists() {
        Optional<Genre> genre = genreRepository.findAllByName("&*^%&*^%$^&%$(^&*^&&*^%$");
        assertTrue(genre.isEmpty());
    }

    @Test
    public void getIdExists() {
        Optional<Genre> optionalGenre = genreRepository.findAllByName("Драма");
        int id = optionalGenre.map(Genre::getId).orElse(null);
        assertEquals(, id);
    }

    @Test
    public void getIdNotExists() {
        Genre match = new Genre("&*^%&*^%$^&%$(^&*^&&*^%$");
        genreRepository.save(match);
        String id = match.getId();
        assertEquals(-1, id);
    }

    @Test
    public void findByIdExisted() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreRepository.findById(1);
        assertTrue(optGenre.isPresent());
        assertEquals(match.getName(), optGenre.get().getName());
    }

    @Test
    public void findByIdNotExisted() {
        Optional<Genre> genre = genreRepository.findById(99999);
        assertTrue(genre.isEmpty());
    }

    @Test
    public void findAll() {
        List<Genre> result = genreRepository.findAll();
        assertEquals(3, result.size());
    }

    @Test
    public void updateExisted() {
        Genre match = new Genre("ЖЗЛ");
        genreRepository.findAllByName(match.getName()).map(Genre::getId)
                .ifPresent(id -> {
                    String newName = "Пародия";
                    genreRepository.save(new Genre(id, newName));
                    Optional<Genre> optGenre = genreRepository.findById(id);
                    assertTrue(optGenre.isPresent());
                    assertEquals(newName, optGenre.get().getName());
                });

    }
}