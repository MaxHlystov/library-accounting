package ru.fmtk.khlystov.booksaccounting.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Author", Long.class);
        return query.getSingleResult();
    }

    @Override
    public void insert(Author author) {
        em.persist(author);
    }

    @Override
    public Optional<Author> findByFullName(String firstName, String secondName) {
        try {
            TypedQuery<Author> query = em.createQuery("select a " +
                            "from Author a " +
                            "where a.first_name = :firstName " +
                            "   and a.second_name = :secondName",
                    Author.class);
            query.setParameter("firstName", firstName);
            query.setParameter("secondName", secondName);
            return Optional.ofNullable(query.getSingleResult());
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<Author> getById(int id) {
        try {
            return Optional.ofNullable(em.find(Author.class, id));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getId(Author author) {
        TypedQuery<Integer> query = em.createQuery("select a.id " +
                        "from Author a " +
                        "where a.first_name = :firstName " +
                        "   and a.second_name = :secondName",
                Integer.class);
        query.setParameter("firstName", author.getFirstName());
        query.setParameter("secondName", author.getSecondName());
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery(
                "select a " +
                        "from Author a " +
                        "order by a.secondName, a.firstName",
                Author.class);
        return query.getResultList();
    }

    @Override
    public boolean update(Author author) {
        try {
            if (author.getId() <= 0) {
                insert(author);
            } else {
                em.merge(author);
            }
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ignored) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Author author) {
        try {
            em.remove(em.contains(author) ? author : em.merge(author));
        } catch (
                ConstraintViolationException ignore) {
            return false;
        }
        return true;
    }
}
