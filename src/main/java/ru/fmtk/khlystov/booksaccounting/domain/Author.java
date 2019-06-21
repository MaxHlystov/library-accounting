package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Author {
    @NotNull
    private final String firstName;
    @NotNull
    private final String secondName;

    public Author(@NotNull String firstName, @NotNull String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
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
        return Objects.equals(firstName, author.firstName) &&
                Objects.equals(secondName, author.secondName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, secondName);
    }

    @Override
    public String toString() {
        return String.format("%s %s", firstName, secondName);
    }
}
