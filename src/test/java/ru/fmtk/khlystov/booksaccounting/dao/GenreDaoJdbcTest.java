package ru.fmtk.khlystov.booksaccounting.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;
import ru.fmtk.khlystov.booksaccounting.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.fmtk.khlystov.booksaccounting.config", "ru.fmtk.khlystov.booksaccounting.repositories"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        String drama = "Драма";
        Optional<Genre> optionalGenre = genreRepository.findAllByName(drama);
        String name = optionalGenre.map(Genre::getName).orElse(null);
        assertEquals(drama, name);
    }

    @Test
    public void getIdNotExists() {
        String nameNotExists = "&*^%&*^%$^&%$(^&*^&&*^%$";
        Genre match = new Genre(nameNotExists);
        Optional<Genre> genres = genreRepository.findAllByName(nameNotExists);
        assertTrue(genres.isEmpty());
    }

    @Test
    public void findByIdExisted() {
        String genreName = "Драма";
        Genre match = new Genre(genreName);
        Optional<Genre> optGenre = genreRepository.findAllByName(genreName);
        assertTrue(optGenre.isPresent());
        assertEquals(genreName, optGenre.get().getName());
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