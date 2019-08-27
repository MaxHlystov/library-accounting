package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class AuthorSql {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name", nullable = false)
    private String secondName;

    public AuthorSql() {
        this("", "");
    }

    public AuthorSql(Author author) {
        this(author.getId(), author.getFirstName(), author.getSecondName());
    }

    public AuthorSql(String id, @NotNull String firstName, @NotNull String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public AuthorSql(@NotNull String firstName, @NotNull String secondName) {
        this(null, firstName, secondName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        AuthorSql author = (AuthorSql) o;
        return Objects.equals(id, author.id) &&
                firstName.equals(author.firstName) &&
                secondName.equals(author.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName);
    }

    @Override
    public String toString() {
        return String.format("%s %s",
                firstName,
                secondName);
    }
}