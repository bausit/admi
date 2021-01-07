package org.bausit.admin.search;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.models.Participant;

@RequiredArgsConstructor
@Log4j2
public class ParticipantPredicate {
    private final SearchCriteria criteria;
    public BooleanExpression getPredicate() {
        PathBuilder<Participant> entityPath = new PathBuilder<>(Participant.class, "participant");

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
        else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
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
