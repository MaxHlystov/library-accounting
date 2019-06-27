package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void insert(Genre genre) {
        em.persist(genre);
    }

    @Override
    public Genre findByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.name LIKE :name",
                Genre.class);
        query.setParameter("name", name);
        return query.getSingleResult();

    }

    @Override
    public Optional<Integer> getId(Genre genre) {
        TypedQuery<Integer> query = em.createQuery("select g.id from Genre g where g.name LIKE :name",
                Integer.class);
        query.setParameter("name", genre.getName());
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Genre getById(int id) {
        return em.find(Genre.class, id);
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();

    }

    @Override
    public void update(Genre genre) {
        int id = genre.getId();
        if (id < 0) {
            em.persist(genre);
        } else {
            em.merge(genre);
        }
    }

    @Override
    public void delete(Genre genre) {
        em.remove(genre);
    }
}
