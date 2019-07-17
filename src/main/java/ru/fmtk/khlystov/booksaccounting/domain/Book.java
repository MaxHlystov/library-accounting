package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "books")
public class Book {
    @Id
    private int id;

    private String title;

    @Nullable
    private String description;

    private Author author;

    private Genre genre;

    private List<Comment> comments;

    public Book() {
        this(-1, "", null, new Author(), new Genre(), null);
    }

    public Book(Book book) {
        this(book.getId(), book.getTitle(), book.getDescription(), book.getAuthor(), book.getGenre(), null);
    }

    public Book(@NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre) {
        this(-1, title, description, author, genre, null);
    }

    public Book(int id,
                @NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre,
                @Nullable List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
        if (comments == null) {
            this.comments = new ArrayList<>();
        } else {
            this.comments = comments;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NotNull
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @NotNull
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                title.equals(book.title) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author);
    }

    @Override
    public String toString() {
        return String.format("#%s %s - %s, жанр: %s",
                (id == -1) ? "-" : Integer.toString(id),
                getAuthor(),
                title,
                getGenre());
    }
}
