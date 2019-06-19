package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Book {
    private final int id;
    private final String title;
    private final String description;
    private final int authorId;
    private final int genreId;

    public Book(int id, @NotNull String title, @Nullable String description, int authorId, int genreId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getGenreId() {
        return genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
