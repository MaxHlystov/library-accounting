package ru.fmtk.khlystov.booksaccounting.repository;

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
        TypedQuery<Integer> query = em.createQuery("select g.id from Genre g where g.name LIKE :name",
                Integer.class);
        query.setParameter("name", genre.getName());
        return Optional.ofNullable(query.getSingleResult());
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
    public int update(Genre genre) {
        try {
            int id = genre.getId();
            if (id < 0) {
                em.persist(genre);
            } else {
                em.merge(genre);
            }
        } catch (EntityExistsException | IllegalArgumentException | TransactionRequiredException ignored) {
            return 0;
        }
        return 1;
    }

    @Override
    public void delete(Genre genre) {
        em.remove(genre);
    }
}
