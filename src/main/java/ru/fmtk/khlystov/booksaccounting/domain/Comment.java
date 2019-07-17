package ru.fmtk.khlystov.booksaccounting.domain;

import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private String id;

    private String text;

    private LocalDateTime date;

    public Comment() {
        this.id = null;
    }

    public Comment(String text) {
        this(null, text, LocalDateTime.now());
    }

    public Comment(String id, String text, LocalDateTime date) {
        this.id = id;
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
        return String.format("%s %t",
                text,
                date);
    }
}
