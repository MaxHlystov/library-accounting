package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.author = :author")
    List<Book> findAllByAuthor(@Param("author") Author author);

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.genre = :genre")
    List<Book> findAllByGenre(@Param("genre") Genre genre);

    List<Book> findAll();
}
