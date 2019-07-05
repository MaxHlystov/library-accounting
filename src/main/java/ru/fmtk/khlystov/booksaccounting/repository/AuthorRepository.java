package ru.fmtk.khlystov.booksaccounting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
    //    long count();
//
//    void insert(Author author);
//
//    Optional<Author> findByFullName(String firstName, String secondName);
//
//    Optional<Author> getById(int id);
//
//    Optional<Integer> getId(Author author);
//
    List<Author> findAll();
//
//    boolean update(Author author);
//
//    boolean delete(Author author);
}
