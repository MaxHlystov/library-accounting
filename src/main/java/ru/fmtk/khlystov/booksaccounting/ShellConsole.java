package ru.fmtk.khlystov.booksaccounting;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.jetbrains.annotations.NotNull;
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
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    public ShellConsole(AuthorDao authorDao, GenreDao genreDao, BookDao bookDao) {
        this.textIO = TextIoFactory.getTextIO();
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookDao = bookDao;
    }

    @ShellMethod(value = "Добавить автора по имени.")
    public String addAuthor(String firstName, String secondName) {
        Author author = new Author(firstName, secondName);
        return authorDao.persist(author).map(
                (id) -> "Автор сохранен в системе с id = " + id.toString())
                .orElse("Не удалось сохранить автора. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Добавить жанр.")
    public String addGenre(String name) {
        Genre genre = new Genre(name);
        return genreDao.persist(genre).map(
                (id) -> "Жанр сохранен в системе с id = " + id.toString())
                .orElse("Не удалось сохранить жанр. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Добавить книгу.")
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
        Optional<Genre> genreOptional = cliObjectSelector(genres,
                "Выберете номер жанра книги:");
        if (genreOptional.isEmpty()) {
            return null;
        }
        Book book = new Book(title, description, authorOptional.get(), genreOptional.get());
        return bookDao.persist(book).map(
                (id) -> "Книга сохранена в системе с id = " + id.toString())
                .orElse("Не удалось сохранить книгу. Попробуйте еще раз.");
    }

    @ShellMethod(value = "Показать список авторов.")
    public void authors() {
        showTable(authorsListToArrayTable(authorDao.getAll()));
    }

    @ShellMethod(value = "Показать список жанров.")
    public String genres() {
        return genreDao.getAll().stream().map(Genre::toString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Показать список книг.")
    public String books() {
        var books = bookDao.getAll();
        if (books.isEmpty()) {
            return "Книги отсутствуют в базе.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    @ShellMethod(value = "Показать книги автора.")
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

    @ShellMethod(value = "Показать книги в указанном жанра.")
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

    @ShellMethod(value = "Изменить автора.", key = {"cha"})
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
                    newAuthor = new Author(author.getId(), newFirstName, newSecondName);
                    if (author.equals(newAuthor)) {
                        return "";
                    }
                    if (authorDao.update(newAuthor) == 1) {
                        return "Автор успешно переименован.";
                    }
                    return "Автор не переименован. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Изменить жанр.", key = {"chg"})
    public String changeGenre() {
        List<Genre> authors = genreDao.getAll();
        return cliObjectSelector(authors, "Выберете номер жанра для переименования:")
                .map(genre -> {
                    String newName = askGenreName("Укажите новое имя жанра.");
                    if (StringUtils.isEmpty(newName) || genre.getName().equals(newName)) {
                        return "";
                    }
                    Genre newGenre = new Genre(genre.getId(), newName);
                    if (genreDao.update(newGenre) == 1) {
                        return "Жанр успешно переименован.";
                    }
                    return "Жанр не переименован. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Удалить автора.")
    public String deleteAuthor() {
        List<Author> authors = authorDao.getAll();
        return cliObjectSelector(authors, "Выберете номер автора для удаления:")
                .map(author -> {
                    authorDao.delete(author);
                    return String.format("Жанр %s удален!", author.toString());
                }).orElse(null);
    }

    @ShellMethod(value = "Удалить жанр.")
    public String deleteGenre() {
        List<Genre> genres = genreDao.getAll();
        return cliObjectSelector(genres, "Выберете номер жанра для удаления:")
                .map(genre -> {
                    genreDao.delete(genre);
                    return String.format("Автор %s удален!", genre.toString());
                }).orElse(null);
    }

    @ShellMethod(value = "Удалить книгу.")
    public String deleteBook() {
        List<Book> books = bookDao.getAll();
        return cliObjectSelector(books, "Выберете номер автора для удаления:")
                .map(book -> {
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
        Stream<String[]> tableTitle = Stream.of(new String[][]{{"#", "Имя", "Фамилия"}});
        return Stream.concat(tableTitle, authors.stream().map(ShellConsole::authorToArray))
                .toArray(Object[][]::new);
    }

    private static Object[] authorToArray(Author author) {
        return new String[]{
                Integer.toString(author.getId()),
                author.getFirstName(),
                author.getSecondName()
        };
    }

    private Object[][] booksListToArrayTable(List<Book> books) {
        Stream<String[]> tableTitle = Stream.of(new String[][]{{"#", "Название", "Автор", "Жанр"}});
        return Stream.concat(tableTitle, books.stream().map(ShellConsole::bookToArray))
                .toArray(Object[][]::new);
    }

    private static Object[] bookToArray(Book book) {
        return new String[]{
                Integer.toString(book.getId()),
                book.getTitle(),
                book.getAuthor().toString(),
                book.getGenre().toString()
        };
    }

    public <T> Optional<T> cliObjectSelector(List<T> items, @NotNull String customPromptMessage) {
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
        var in = textIO.newStringInputReader().withMinLength(0);
        out.println(prompt);
        String firstName = in.read("Введите имя автора (оставьте пустым, есле не хотите изменять):");
        String secondName = in.read("Введите фамилию автора (оставьте пустым, есле не хотите изменять):");
        return new Author(firstName, secondName);
    }

    public String askGenreName(@NotNull String prompt) {
        var out = textIO.getTextTerminal();
        var in = textIO.newStringInputReader().withMinLength(0);
        out.println(prompt);
        return in.read("Введите название (оставьте пустым, есле не хотите изменять):");
    }
}
