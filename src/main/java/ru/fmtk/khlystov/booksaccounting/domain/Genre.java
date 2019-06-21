package ru.fmtk.khlystov.booksaccounting.domain;

import java.util.Objects;

public class Genre {
    private final int Id;
    private final String name;

    public Genre(int id, String name) {
        Id = id;
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Id == genre.Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return name + " (id " + Id + ")";
    }
}
