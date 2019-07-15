package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "authors")
public class Author {
    @Id
    private int id;

    private String firstName;

    private String secondName;

    public Author() {
        this(-1, "", "");
    }

    public Author(int id, @NotNull String firstName, @NotNull String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Author(@NotNull String firstName, @NotNull String secondName) {
        this(-1, firstName, secondName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    @NotNull
    public String getSecondName() {
        return secondName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id == author.id &&
                firstName.equals(author.firstName) &&
                secondName.equals(author.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName);
    }

    @Override
    public String toString() {
        return String.format("#%s %s %s",
                (id == -1) ? "-" : Integer.toString(id),
                firstName,
                secondName);
    }
}
