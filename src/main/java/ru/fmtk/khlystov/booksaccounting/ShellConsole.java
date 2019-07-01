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
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;
import ru.fmtk.khlystov.booksaccounting.repository.AuthorRepository;
import ru.fmtk.khlystov.booksaccounting.repository.BookRepository;
import ru.fmtk.khlystov.booksaccounting.repository.CommentRepository;
import ru.fmtk.khlystov.booksaccounting.repository.GenreRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
public class ShellConsole {
    private final TextIO textIO;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public ShellConsole(AuthorRepository authorRepository, GenreRepository genreRepository,
                        BookRepository bookRepository, CommentRepository commentRepository) {
        this.textIO = TextIoFactory.getTextIO();
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @ShellMethod(value = "Добавить автора по имени.")
    public String addAuthor(String firstName, String secondName) {
        Author author = new Author(firstName, secondName);
        if (authorRepository.update(author)) {
            return String.format("Автор сохранен в системе %d", author.getId());
        }
        return "Не удалось сохранить автора. Попробуйте еще раз.";
    }

    @ShellMethod(value = "Добавить жанр.")
    public String addGenre(String name) {
        Genre genre = new Genre(name);
        if (genreRepository.update(genre)) {
            return String.format("Жанр сохранен в системе с id = %d", genre.getId());
        }
        return "Не удалось сохранить жанр. Попробуйте еще раз.";
    }

    @ShellMethod(value = "Добавить книгу.")
    public String addBook(@ShellOption({"-t", "--title"}) String title,
                          @ShellOption(value = {"-d", "--descr"},
                                  defaultValue = "") String description) {
        List<Author> authors = authorRepository.getAll();
        Optional<Author> authorOptional = cliObjectSelector(authors,
                "Выберете номер автора для книги:");
        if (authorOptional.isEmpty()) {
            return null;
        }
        List<Genre> genres = genreRepository.getAll();
        Optional<Genre> genreOptional = cliObjectSelector(genres,
                "Выберете номер жанра книги:");
        if (genreOptional.isEmpty()) {
            return null;
        }
        Book book = new Book(title, description, authorOptional.get(), genreOptional.get());
        return bookRepository.update(book)
                ? "Книга сохранена в системе с id = " + book.getId()
                : "Не удалось сохранить книгу. Попробуйте еще раз.";
    }

    @ShellMethod(value = "Добавить комментарий к книге.")
    public String commentBook(@ShellOption({"-t", "--text"}) String text) {
        List<Book> books = bookRepository.getAll();
        Optional<Book> optionalBook = cliObjectSelector(books,
                "Выберете номер книги для добавления комментария:");
        if (optionalBook.isEmpty()) {
            return null;
        }
        Comment comment = new Comment(text);
        commentRepository.addComment(comment);
        return null;
    }

    @ShellMethod(value = "Узнать количество книг.", key = {"bcount"})
    public String booksCount() {
        return Long.toString(bookRepository.count());
    }

    @ShellMethod(value = "Узнать количество авторов.", key = {"acount"})
    public String authorsCount() {
        return Long.toString(authorRepository.count());
    }

    @ShellMethod(value = "Узнать количество жанров.", key = {"gcount"})
    public String genresCount() {
        return Long.toString(genreRepository.count());
    }

    @ShellMethod(value = "Показать список авторов.")
    public void authors() {
        showTable(authorsListToArrayTable(authorRepository.getAll()));
    }

    @ShellMethod(value = "Показать список жанров.")
    public String genres() {
        return genreRepository.getAll().stream().map(Genre::toString)
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Показать список книг.")
    public String books() {
        var books = bookRepository.getAll();
        if (books.isEmpty()) {
            return "Книги отсутствуют в базе.";
        }
        showTable(booksListToArrayTable(books));
        return null;
    }

    @ShellMethod(value = "Показать книги автора.")
    public String listByAuthor() {
        List<Author> authors = authorRepository.getAll();
        return cliObjectSelector(authors, "Выберете номер автора для просмотра его книг:")
                .map(author -> {
                    var books = bookRepository.getByAuthor(author);
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
        List<Genre> genres = genreRepository.getAll();
        Optional<Genre> selected = cliObjectSelector(genres,
                "Выберете номер жанра для просмотра книг:");
        return selected.map(genre -> {
            var books = bookRepository.getByGenre(genre);
            if (books.isEmpty()) {
                return "Не удалось получить список книг указанного жанра. Попробуйте еще раз.";
            }
            showTable(booksListToArrayTable(books));
            return null;
        }).orElse(null);
    }

    @ShellMethod(value = "Показать комментарии к книге.")
    public String comments() {
        var books = bookRepository.getAll();
        cliObjectSelector(books, "Выберете номер книги чтобы посмотреть комментарии:")
                .ifPresent(book -> {
                    var bookComments = commentRepository.getForBook(book);
                    showTable(commentsListToArrayTable(bookComments));
                });
        return null;
    }

    @ShellMethod(value = "Переименовать книгу.")
    public String renameBook() {
        List<Book> books = bookRepository.getAll();
        return cliObjectSelector(books, "Выберете номер книги для переименования:")
                .map(book -> {
                    String newTitle = askBookTitle("Укажите новое название книги:");
                    if (StringUtils.isEmpty(newTitle)
                            || Objects.equals(newTitle, book.getTitle())) {
                        return null;
                    }
                    Book newBook = new Book(book);
                    newBook.setTitle(newTitle);
                    if (bookRepository.update(newBook)) {
                        return "Книга успешно переименована.";
                    }
                    return "Книга не переименована. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Изменить автора книги.", key = {"chba"})
    public String changeBookAuthor() {
        List<Book> books = bookRepository.getAll();
        return cliObjectSelector(books, "Выберете номер книги для изменения:")
                .flatMap(book -> {
                    List<Author> authors = authorRepository.getAll();
                    return cliObjectSelector(authors, "Выберете номер автора книги:")
                            .map(newAuthor -> {
                                if (newAuthor.equals(book.getAuthor())) {
                                    return null;
                                }
                                Book newBook = new Book(book);
                                newBook.setAuthor(newAuthor);
                                if (bookRepository.update(newBook)) {
                                    return "Автор успешно изменен.";
                                }
                                return "Автор не изменен. Попробуйте еще раз.";
                            });
                })
                .orElse("");
    }

    @ShellMethod(value = "Изменить жанр книги.", key = {"chbg"})
    public String changeBookGenre() {
        List<Book> books = bookRepository.getAll();
        return cliObjectSelector(books, "Выберете номер книги для изменения:")
                .flatMap(book -> {
                    List<Genre> genres = genreRepository.getAll();
                    return cliObjectSelector(genres, "Выберете номер жанра книги:")
                            .map(newGenre -> {
                                if (newGenre.equals(book.getGenre())) {
                                    return null;
                                }
                                Book newBook = new Book(book);
                                newBook.setGenre(newGenre);
                                if (bookRepository.update(newBook)) {
                                    return "Жанр успешно изменен.";
                                }
                                return "Жанр не изменен. Попробуйте еще раз.";
                            });
                })
                .orElse("");
    }

    @ShellMethod(value = "Переименовать автора.", key = {"cha"})
    public String changeAuthor() {
        List<Author> authors = authorRepository.getAll();
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
                    if (authorRepository.update(newAuthor)) {
                        return "Автор успешно переименован.";
                    }
                    return "Автор не переименован. Попробуйте еще раз.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Переименовать жанр.", key = {"chg"})
    public String changeGenre() {
        List<Genre> genres = genreRepository.getAll();
        return cliObjectSelector(genres, "Выберете номер жанра для переименования:")
                .map(genre -> {
                    String newName = askGenreName("Укажите новое имя жанра.");
                    if (StringUtils.isEmpty(newName) || genre.getName().equals(newName)) {
                        return "";
                    }
                    Genre newGenre = new Genre(genre.getId(), newName);
                    genreRepository.update(newGenre);
                    return "Жанр успешно переименован.";
                })
                .orElse("");
    }

    @ShellMethod(value = "Удалить автора.")
    public String deleteAuthor() {
        List<Author> authors = authorRepository.getAll();
        return cliObjectSelector(authors, "Выберете номер автора для удаления:")
                .map(author -> {
                    authorRepository.delete(author);
                    return String.format("Жанр %s удален!", author.toString());
                }).orElse(null);
    }

    @ShellMethod(value = "Удалить жанр.")
    public String deleteGenre() {
        List<Genre> genres = genreRepository.getAll();
        return cliObjectSelector(genres, "Выберете номер жанра для удаления:")
                .map(genre -> {
                    genreRepository.delete(genre);
                    return String.format("Автор %s удален!", genre.toString());
                }).orElse(null);
    }

    @ShellMethod(value = "Удалить книгу.")
    public String deleteBook() {
        List<Book> books = bookRepository.getAll();
        return cliObjectSelector(books, "Выберете номер автора для удаления:")
                .map(book -> {
                    bookRepository.delete(book);
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

    private static Object[][] commentsListToArrayTable(List<Comment> comments) {
        Stream<String[]> tableTitle = Stream.of(new String[][]{{"#", "Комментарий", "Дата"}});
        return Stream.concat(tableTitle, comments.stream().map(ShellConsole::commentToArray))
                .toArray(Object[][]::new);
    }

    private static Object[] commentToArray(Comment comment) {
        return new String[]{
                Integer.toString(comment.getId()),
                comment.getText(),
                comment.getDate().toString()
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

    public String askBookTitle(@NotNull String prompt) {
        var out = textIO.getTextTerminal();
        var in = textIO.newStringInputReader().withMinLength(0);
        out.println(prompt);
        return in.read("Введите название (оставьте пустым, есле не хотите изменять):");
    }
}
