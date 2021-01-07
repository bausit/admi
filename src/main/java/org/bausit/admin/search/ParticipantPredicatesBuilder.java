package org.bausit.admin.search;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public class ParticipantPredicatesBuilder {
    private List<SearchCriteria> params;

    public ParticipantPredicatesBuilder() {
        params = new ArrayList<>();
    }

    public ParticipantPredicatesBuilder with(
        String key, String operation, Object value) {

        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        BooleanExpression result = Expressions.asBoolean(true).isTrue();

        if (params.size() == 0) {
            log.info("no parameters found");
            return result;
        }

        List<BooleanExpression> predicates = params.stream()
            .map(param -> new ParticipantPredicate(param).getPredicate())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        for (BooleanExpression predicate : predicates) {
            log.info("adding: {}", predicate);
            result = result.and(predicate);
        }
        return result;
    }
}
