package ru.fmtk.khlystov.booksaccounting.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fmtk.khlystov.booksaccounting.domain.AuthorSql;

public interface AuthorSqlJpaRepository extends JpaRepository<AuthorSql, Long> {
}
