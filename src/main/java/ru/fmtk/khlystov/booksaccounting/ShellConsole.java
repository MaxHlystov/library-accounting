package ru.fmtk.khlystov.booksaccounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import ru.fmtk.khlystov.booksaccounting.dao.AuthorDao;
import ru.fmtk.khlystov.booksaccounting.dao.BookDao;
import ru.fmtk.khlystov.booksaccounting.dao.GenreDao;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @ShellMethod(value = "Add an author by name.")
    public String addAuthor(String firstName, String secondName) {
        Author author = new Author(firstName, secondName);
        return authorDao.persist(author).map(
                (id) -> "Автор сохранен в системе с id = " + id.toString())
                .orElse("Не удалось сохранить автора. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Add a genre.")
    public String addGenre(String name) {
        Genre genre = new Genre(name);
        return genreDao.persist(genre).map(
                (id) -> "Жанр сохранен в системе с id = " + id.toString())
                .orElse("Не удалось сохранить жанр. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Add a book.")
    public String addBook(@ShellOption({"-t", "--title"}) String title,
                          @ShellOption({"-f", "--first"}) String authorFirstName,
                          @ShellOption({"-s", "--second"}) String authorSecondName,
                          @ShellOption({"-g", "--genre"}) String genreName,
                          @ShellOption(value = {"-d", "--descr"},
                                  defaultValue = "") String description) {
        Author author = new Author(authorFirstName, authorSecondName);
        Genre genre = new Genre(genreName);
        Book book = new Book(title, description, author, genre);
        return bookDao.persist(book).map(
                (id) -> "Книга сохранена в системе с id = " + id.toString())
                .orElse("Не удалось сохранить книгу. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Show all authors.")
    public String authors() {
        showTable(authorsListToArrayTable(authorDao.getAll()));
        return null;
    }

    @ShellMethod(value = "Show all genres.")
    public String genres() {
        return genreDao.getAll().stream().map(Genre::toString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Show all books.")
    public String books() {
        var books = bookDao.getAll();
        if(books.isEmpty()) {
            return "Книги отсутствуют в базе.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    @ShellMethod(value = "List books by author.")
    public String listByAuthor(String firstName, String secondName) {
        Author author = new Author(firstName, secondName);
        var books = bookDao.getByAuthor(author);
        if (books.isEmpty()) {
            return "Не удалось получить список книг для автора. Попробуйте еще раз.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    @ShellMethod(value = "List books by author.")
    public String listByGenre(String genre) {
        var books = bookDao.getByGenre(new Genre(genre));
        if (books.isEmpty()) {
            return "Не удалось получить список книг указанного жанра. Попробуйте еще раз.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    private void showTable(Object[][] table) {
        TableModel model = new ArrayTableModel(table);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        System.out.println(tableBuilder.build().render(80));
    }

    private Object[][] authorsListToArrayTable(List<Author> authors) {
        Stream<String[]> tableTitle = Stream.of(new String[][] {{"First name", "Second name"}});
        return Stream.concat(tableTitle, authors.stream().map(ShellConsole::authorToArray))
                .toArray(String[][]::new);
    }

    private static Object[] authorToArray(Author author) {
        return new String[]{
                author.getFirstName(),
                author.getSecondName()
        };
    }

    private Object[][] booksListToArrayTable(List<Book> books) {
        Stream<String[]> tableTitle = Stream.of(new String[][] {{"Title", "Author", "Genre"}});
        return Stream.concat(tableTitle, books.stream().map(ShellConsole::bookToArray))
                .toArray(String[][]::new);
    }

    private static Object[] bookToArray(Book book) {
        return new String[]{
                book.getTitle(),
                book.getAuthor().toString(),
                book.getGenre().toString()
        };
    }
}
