package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Genre {
    private int id;
    @NotNull
    private final String name;

    public Genre(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(@NotNull String name) {
        this(-1, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return id == genre.id &&
                name.equals(genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return String.format("#%s %s",
                (id == -1) ? "-" : Integer.toString(id),
                name);
    }
}
