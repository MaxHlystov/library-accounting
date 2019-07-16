package ru.fmtk.khlystov.booksaccounting.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "BookWithAuthorAndGenre",
        attributeNodes = {@NamedAttributeNode(value = "author"),
                @NamedAttributeNode(value = "genre")})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    @Nullable
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) //fetch = FetchType.LAZY,
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public Book() {
        this(-1, "", null, new Author(), new Genre());
    }

    public Book(Book book) {
        this(book.getId(), book.getTitle(), book.getDescription(), book.getAuthor(), book.getGenre());
    }

    public Book(long id,
                @NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
    }

    public Book(@NotNull String title,
                @Nullable String description,
                @NotNull Author author,
                @NotNull Genre genre) {
        this(-1, title, description, author, genre);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @NotNull
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @NotNull
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                title.equals(book.title) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author);
    }

    @Override
    public String toString() {
        return String.format("#%s %s - %s, жанр: %s",
                (id == -1) ? "-" : Long.toString(id),
                getAuthor(),
                title,
                getGenre());
    }
}
