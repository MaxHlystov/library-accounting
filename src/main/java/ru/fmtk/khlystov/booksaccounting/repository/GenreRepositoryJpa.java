package ru.fmtk.khlystov.booksaccounting.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Genre", Long.class);
        return query.getSingleResult();
    }

    @Override
    public void insert(Genre genre) {
        em.persist(genre);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try {
            TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name LIKE :name",
                    Genre.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.getSingleResult());
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getId(Genre genre) {
        TypedQuery<Integer> query = em.createQuery("select g.id from Genre g where g.name = :name",
                Integer.class);
        query.setParameter("name", genre.getName());
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException | NonUniqueResultException | QueryTimeoutException ignored){
        }
        return Optional.empty();
    }

    @Override
    public Optional<Genre> getById(int id) {
        try {
            Optional.ofNullable(em.find(Genre.class, id));
        } catch (IllegalArgumentException ignored) {
        }
        return Optional.empty();
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g order by g.name", Genre.class);
        return query.getResultList();

    }

    @Override
    public boolean update(Genre genre) {
        try {
            if (genre.getId() <= 0) {
                em.persist(genre);
            } else {
                em.merge(genre);
            }
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ignored) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Genre genre) {
        try {
            em.remove(em.contains(genre) ? genre : em.merge(genre));
        } catch (ConstraintViolationException ignore) {
            return false;
        }
        return true;
    }
}
