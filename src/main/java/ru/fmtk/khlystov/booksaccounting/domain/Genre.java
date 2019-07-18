package ru.fmtk.khlystov.booksaccounting.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    public Genre() {
        this(-1, "");
    }

    public Genre(String name) {
        this(-1, name);
    }

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
                (id == -1) ? "-" : Long.toString(id),
                name);
    }
}
