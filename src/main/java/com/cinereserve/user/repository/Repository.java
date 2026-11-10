package com.cinereserve.user.repository;

import java.util.List;
import java.util.Optional;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION (GENERIC INTERFACE)
 * =========================================================================
 * Defines the standard CRUD contract for persistence, decoupling the business
 * logic from the underlying storage mechanism (Text File I/O).
 */
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean save(T entity);
    boolean update(T entity);
    boolean deleteById(ID id);
    boolean existsById(ID id);
}
