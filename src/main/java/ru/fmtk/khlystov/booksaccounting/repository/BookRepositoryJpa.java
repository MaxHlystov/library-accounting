package ru.fmtk.khlystov.booksaccounting.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Book", Long.class);
        return query.getSingleResult();
    }

    @Override
    public void insert(Book book) {
        em.persist(book);
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, Author author) {
        TypedQuery<Book> query = em.createQuery(
                "select b " +
                        "from Book b " +
                        "where b.title = :title" +
                        "   and b.author = :author",
                Book.class);
        query.setParameter("title", title);
        query.setParameter("author", author);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Book> getById(int id) {
        try {
            Optional.ofNullable(em.find(Book.class, id));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getId(Book book) {
        return findByTitleAndAuthor(book.getTitle(), book.getAuthor())
                .map(bookFound -> bookFound.getId());
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        TypedQuery<Book> query = em.createQuery(
                "select b " +
                        "from Book b " +
                        "where b.author = :author " +
                        "order by b.title", Book.class);
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        TypedQuery<Book> query = em.createQuery(
                "select b " +
                        "from Book b " +
                        "where b.genre = :genre " +
                        "order by b.title", Book.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b order by b.title", Book.class);
        return query.getResultList();
    }

    @Override
    public boolean update(Book book) {
        try {
            book.setAuthor(AuxillaryMethods.mergeIfNeeded(em, book.getAuthor()));
            book.setGenre(AuxillaryMethods.mergeIfNeeded(em, book.getGenre()));
            if (book.getId() <= 0) {
                em.persist(book);
            } else {
                em.merge(book);
            }
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ignored) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Book book) {
        try {
            em.remove(AuxillaryMethods.mergeIfNeeded(em, book));
        } catch (ConstraintViolationException ignore) {
            return false;
        }
        return true;
    }
}

