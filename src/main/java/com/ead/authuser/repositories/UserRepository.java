package com.ead.authuser.repositories;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)//Anotação para definir que determinado atributo em uma determinada consulta, ira utilizar o Fetch invés do Lazy.
    Optional<UserModel> findByUsername(String username);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)//Anotação para definir que determinado atributo em uma determinada consulta, ira utilizar o Fetch invés do Lazy.
    Optional<UserModel> findById(UUID userId);
}
