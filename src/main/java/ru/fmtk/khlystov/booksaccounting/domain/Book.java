package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Book {
    private int id;
    @NotNull
    private final String title;
    @Nullable
    private final String description;
    @NotNull
    private final Author author;
    @NotNull
    private final Genre genre;

    public Book(int id,
                @NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
    }

    public Book(@NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre) {
        this(-1, title, description, author, genre);
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

    @Nullable
    public String getDescription() {
        return description;
    }

    @NotNull
    public Author getAuthor() {
        return author;
    }

    @NotNull
    public Genre getGenre() {
        return genre;
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
