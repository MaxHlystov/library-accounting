package ru.fmtk.khlystov.booksaccounting.repository;

import javax.persistence.EntityManager;

public class AuxillaryMethods {
    public static <T> T mergeIfNeeded(EntityManager em, T entity) {
        return em.contains(entity) ? entity : em.merge(entity);
    }
}
