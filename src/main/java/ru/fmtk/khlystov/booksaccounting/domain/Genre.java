package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Genres")
public class Genre {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    public Genre() {

    }

    public Genre(int id, String name) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
