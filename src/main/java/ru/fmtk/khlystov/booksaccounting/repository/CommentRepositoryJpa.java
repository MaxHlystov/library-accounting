package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.stereotype.Repository;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> getForBook(Book book) {
        TypedQuery<Comment> query = em.createQuery(
                "select c " +
                        "from Comment c " +
                        "where c.book = :book " +
                        "order by c.date desc ", Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public void addComment(Comment comment) {
        comment.setBook(AuxillaryMethods.mergeIfNeeded(em, comment.getBook()));
        em.persist(comment);
    }
}
