package org.bausit.admin.search;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class PredicatesBuilder {
    public static final Pattern PATTERN = Pattern.compile("(\\w+?)(:|<|>)([\\w-]+?),",
        Pattern.UNICODE_CHARACTER_CLASS);

    private final Class clazz;
    private List<SearchCriteria> params;
    private List<Predicate> addedPredicates;

    public PredicatesBuilder(Class clazz) {
        this.clazz = clazz;
        params = new ArrayList<>();
        addedPredicates = new ArrayList<>();
    }

    public PredicatesBuilder with(Predicate predicate) {
        addedPredicates.add(predicate);
        return this;
    }

    public PredicatesBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        BooleanExpression result = Expressions.asBoolean(true).isTrue();

        List<BooleanExpression> predicates = params.stream()
            .map(param -> new EntityPredicate(param, clazz).getPredicate())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }

        for(Predicate p : addedPredicates) {
            result = result.and(p);
        }

        return result;
    }
}
