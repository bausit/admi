package org.bausit.admin.repositories;

import com.querydsl.core.types.dsl.StringPath;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.QEvent;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long>,
    QuerydslPredicateExecutor<Event>,
    QuerydslBinderCustomizer<QEvent> {

    List<Event> findByDateBetween(Instant start, Instant end);

    @Override
    default void customize(QuerydslBindings bindings, QEvent event) {
        bindings.bind(String.class)
            .first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
