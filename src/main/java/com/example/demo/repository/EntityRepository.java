package com.example.demo.repository;

import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

import com.example.demo.model.ExampleEntity;
import jakarta.persistence.QueryHint;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface EntityRepository extends JpaRepository<ExampleEntity, Long> {
    @QueryHints(value = {
            @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE),
            @QueryHint(name = HINT_CACHEABLE, value = "false"),
            @QueryHint(name = HINT_READONLY, value = "true")})
    Stream<ExampleEntity> findAllBy();
}
