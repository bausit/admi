package org.bausit.admin.repositories;

import com.querydsl.core.types.dsl.StringPath;
import org.bausit.admin.models.Member;
import org.bausit.admin.models.QMember;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long>,
    QuerydslPredicateExecutor<Member>,
    QuerydslBinderCustomizer<QMember> {

    @Override
    default void customize(QuerydslBindings bindings, QMember member) {
        bindings.bind(String.class)
            .first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(member.password);
    }
}
