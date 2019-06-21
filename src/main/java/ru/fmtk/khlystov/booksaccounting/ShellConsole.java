package ru.fmtk.khlystov.booksaccounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.fmtk.khlystov.booksaccounting.dao.AuthorDao;
import ru.fmtk.khlystov.booksaccounting.dao.BookDao;
import ru.fmtk.khlystov.booksaccounting.dao.GenreDao;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@ShellComponent
public class ShellConsole {
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    @Autowired
    public ShellConsole(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @PostConstruct
    public void init() {
        clearState();
    }

    private void clearState() {

    }

    @ShellMethod(value = "Show all authors.")
    public String authors() {
        return authorDao.getAll().stream().map(Author::toString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Show all genres.")
    public String genres() {
        return genreDao.getAll().stream().map(Genre::toString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Show all books.")
    public String books() {
        return bookDao.getAll().stream().map(Book::toString)
                .collect(Collectors.joining("\n"));
    }
}
