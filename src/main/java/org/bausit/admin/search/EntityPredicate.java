package org.bausit.admin.search;

import com.querydsl.core.types.dsl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Participant;

@RequiredArgsConstructor
@Log4j2
public class EntityPredicate {
    private final SearchCriteria criteria;
    private final Class clazz;
    public BooleanExpression getPredicate() {
        PathBuilder<Participant> entityPath = new PathBuilder<>(clazz, clazz.getSimpleName().toLowerCase());

        if (isNumeric(criteria.getValue())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(value);
                case ">":
                    return path.gt(value);
                case "<":
                    return path.lt(value);
            }
        }
        else if (criteria.getValue() instanceof String) {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        else {
            PathBuilder<Object> path = entityPath.get(criteria.getKey());
            return path.eq(criteria.getValue());
        }

        return null;
    }

    private boolean isNumeric(Object value) {
        try {
            Integer.parseInt((String)value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
