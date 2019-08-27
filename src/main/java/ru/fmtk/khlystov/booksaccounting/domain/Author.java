package ru.fmtk.khlystov.booksaccounting.domain;

import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "AUTHORS")
@Document(collection = "authors")
public class Author {
    @Id
    private String id;

    private String firstName;

    private String secondName;

    public Author() {
        this("", "");
    }

    public Author(String id, @NotNull String firstName, @NotNull String secondName) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Author(@NotNull String firstName, @NotNull String secondName) {
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
        Author author = (Author) o;
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
