package ru.fmtk.khlystov.booksaccounting.domain;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String text;

    private LocalDateTime date;

    @DBRef
    private Book book;

    public Comment() {
        this(null, null);
    }

    public Comment(Book book, String text) {
        this(null, book, text, LocalDateTime.now());
    }

    public Comment(String id, Book book, String text, LocalDateTime date) {
        this.id = id;
        this.book = book;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Book getBook() {
        return book;
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

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                text.equals(comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }

    @Override
    public String toString() {
        if (Strings.isEmpty(text)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
            return "Empty comment at " + date.format(dateTimeFormatter);
        }
        return String.format("%s %t",
                text,
                date);
    }
}
