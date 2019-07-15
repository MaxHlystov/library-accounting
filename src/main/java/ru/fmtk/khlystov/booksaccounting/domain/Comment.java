package ru.fmtk.khlystov.booksaccounting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "Comments")
public class Comment {
    @Id
    private int id;

    private Book book;

    private String text;

    private LocalDateTime date;

    public Comment() {
        this.id = -1;
    }

    public Comment(Book book, String text) {
        this(-1, book, text, LocalDateTime.now());
    }

    public Comment(int id, Book book, String text, LocalDateTime date) {
        this.id = id;
        this.book = book;
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment genre = (Comment) o;
        return id == genre.id &&
                text.equals(genre.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        return String.format("#%s %s %t",
                (id == -1) ? "-" : Integer.toString(id),
                text,
                date);
    }
}
