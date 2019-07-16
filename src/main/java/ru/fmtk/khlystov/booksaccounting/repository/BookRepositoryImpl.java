package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Book;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    public BookRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("BookWithAuthorAndGenre");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }
}
