package ru.fmtk.khlystov.booksaccounting;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.util.StringUtils;
import ru.fmtk.khlystov.booksaccounting.dao.AuthorDao;
import ru.fmtk.khlystov.booksaccounting.dao.BookDao;
import ru.fmtk.khlystov.booksaccounting.dao.GenreDao;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
public class ShellConsole {
    private final TextIO textIO;
    private AuthorDao authorDao;
    private GenreDao genreDao;
    private BookDao bookDao;

    @Autowired
    public ShellConsole(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.textIO = TextIoFactory.getTextIO();
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
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
                          @ShellOption(value = {"-d", "--descr"},
                                  defaultValue = "") String description) {
        List<Author> authors = authorDao.getAll();
        Optional<Author> authorOptional = cliObjectSelector(authors,
                "Выберете номер автора для книги:");
        if (authorOptional.isEmpty()) {
            return null;
        }
        List<Genre> genres = genreDao.getAll();
        Optional<Genre> genreOptional = cliObjectSelector(genres, "Выберете номер жанра книги:");
        if (genreOptional.isEmpty()) {
            return null;
        }
        Book book = new Book(title, description, authorOptional.get(), genreOptional.get());
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
        if (books.isEmpty()) {
            return "Книги отсутствуют в базе.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    @ShellMethod(value = "List books by author.")
    public String listByAuthor() {
        List<Author> authors = authorDao.getAll();
        return cliObjectSelector(authors, "Выберете номер автора для просмотра его книг:")
                .map(author -> {
                    var books = bookDao.getByAuthor(author);
                    if (books.isEmpty()) {
                        return "Не удалось получить список книг для автора. Попробуйте еще раз.";
                    }
                    showTable(booksListToArrayTable(books));
                    return null;
                })
                .orElse("");
    }

    @ShellMethod(value = "List books by author.")
    public String listByGenre() {
        List<Genre> genres = genreDao.getAll();
        Optional<Genre> selected = cliObjectSelector(genres,
                "Выберете номер жанра для удаления:");
        return selected.map(genre -> {
            var books = bookDao.getByGenre(genre);
            if (books.isEmpty()) {
                return "Не удалось получить список книг указанного жанра. Попробуйте еще раз.";
            }
            showTable(booksListToArrayTable(books));
            return null;
        }).orElse(null);
    }

    @ShellMethod(value = "Change author.", key = {"cha"})
    public String changeAuthor() {
        List<Author> authors = authorDao.getAll();
        return cliObjectSelector(authors, "Выберете номер автора для переименования:")
                .map(author -> {
                    Author newAuthor = askAuthor("Укажите новые данные автора.");
                    String newFirstName = newAuthor.getFirstName();
                    String newSecondName = newAuthor.getSecondName();
                    if (StringUtils.isEmpty(newFirstName)) {
                        newFirstName = author.getFirstName();
                    }
                    if (StringUtils.isEmpty(newSecondName)) {
                        newSecondName = author.getSecondName();
                    }
                    newAuthor = new Author(newFirstName, newSecondName);
                    if (author.equals(newAuthor)) {
                        return "";
                    }
                    if (authorDao.update(author, newAuthor) == 1) {
                        return "Автор успешно переименован.";
                    }
                    return "Автор не переименован. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Change genre.", key = {"chg"})
    public String changeGenre() {
        List<Genre> authors = genreDao.getAll();
        return cliObjectSelector(authors, "Выберете номер жанрта для переименования:")
                .map(genre -> {
                    Genre newGenre = askGenre("Укажите новое имя жанра.");
                    String newName = newGenre.getName();
                    if (StringUtils.isEmpty(newName) || genre.getName().equals(newName)) {
                        return "";
                    }
                    newGenre = new Genre(newName);
                    if (genreDao.update(genre, newGenre) == 1) {
                        return "Жанр успешно переименован.";
                    }
                    return "Жанр не переименован. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Delete author.")
    public String deleteAuthor() {
        List<Author> authors = authorDao.getAll();
        Optional<Author> selected = cliObjectSelector(authors,
                "Выберете номер автора для удаления:");
        return selected.map(author -> {
            authorDao.delete(author);
            return String.format("Жанр %s удален!", author.toString());
        }).orElse(null);
    }

    @ShellMethod(value = "Delete genre.")
    public String deleteGenre() {
        List<Genre> genres = genreDao.getAll();
        Optional<Genre> selected = cliObjectSelector(genres,
                "Выберете номер жанра для удаления:");
        return selected.map(genre -> {
            genreDao.delete(genre);
            return String.format("Автор %s удален!", genre.toString());
        }).orElse(null);
    }

    @ShellMethod(value = "Delete book.")
    public String deleteBook() {
        List<Book> books = bookDao.getAll();
        Optional<Book> selected = cliObjectSelector(books,
                "Выберете номер автора для удаления:");
        return selected.map(book -> {
            bookDao.delete(book);
            return String.format("Книга %s удалена!", book.toString());
        }).orElse(null);
    }

    private void showTable(Object[][] table) {
        TableModel model = new ArrayTableModel(table);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        System.out.println(tableBuilder.build().render(80));
    }

    private Object[][] authorsListToArrayTable(List<Author> authors) {
        Stream<String[]> tableTitle = Stream.of(new String[][]{{"Имя", "Фамилия"}});
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
        Stream<String[]> tableTitle = Stream.of(new String[][]{{"Название", "Автор", "Жанр"}});
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

    public <T> Optional<T> cliObjectSelector(List<T> items,
                                             @NotNull String customPromptMessage) {
        if (items == null || items.isEmpty()) {
            return Optional.empty();
        }
        List<String> optionsList = items.stream()
                .map(item -> String.format("[%d] - %s", items.indexOf(item), item))
                .collect(Collectors.toList());
        optionsList.add(0, customPromptMessage);
        textIO.getTextTerminal().println(optionsList);
        Integer selected = textIO.newIntInputReader()
                .withMinVal(0)
                .withMaxVal(optionsList.size() - 1)
                .read("Номер варианта:");
        return Optional.ofNullable(items.get(selected));
    }

    public Author askAuthor(@NotNull String prompt) {
        var out = textIO.getTextTerminal();
        var in = textIO.newStringInputReader();
        out.println(prompt);
        String firstName = in.read("Введите имя автора (оставьте пустым, есле не хотите изменять):");
        String secondName = in.read("Введите фамилию автора (оставьте пустым, есле не хотите изменять):");
        return new Author(firstName, secondName);
    }

    public Genre askGenre(@NotNull String prompt) {
        var out = textIO.getTextTerminal();
        var in = textIO.newStringInputReader();
        out.println(prompt);
        String name = in.read("Введите название (оставьте пустым, есле не хотите изменять):");
        return new Genre(name);
    }
}
