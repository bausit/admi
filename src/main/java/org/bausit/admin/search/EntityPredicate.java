package org.bausit.admin.search;

import com.querydsl.core.types.dsl.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ClassUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Log4j2
public class EntityPredicate {
    private final SearchCriteria criteria;
    private final Class clazz;
    private Map<String, Class> fields;
    private final DateTimeFormatter instantFormat = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" , Locale.US )
        .withZone(ZoneId.systemDefault());

    public EntityPredicate(SearchCriteria criteria, Class clazz) {
        this.criteria = criteria;
        this.clazz = clazz;

        fields = new HashMap<>();
        Arrays.stream(clazz.getDeclaredFields())
            .forEach(field -> fields.put(field.getName(), field.getType()));
    }

    public BooleanExpression getPredicate() {
        PathBuilder entityPath = new PathBuilder<>(clazz, clazz.getSimpleName().toLowerCase());
        Class fieldClass = fields.get(criteria.getKey());
        if(String.class.equals(fieldClass)) {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        else if(fieldClass.equals(Instant.class)) {
            TimePath<Instant> path = entityPath.getTime(criteria.getKey(), Instant.class);
            Instant value = Instant.from(instantFormat.parse(criteria.getValue().toString() + " 00:00:00"));
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(value);
                case ">":
                    return path.gt(value);
                case "<":
                    return path.lt(value);
            }
        }
        else if (ClassUtils.isPrimitiveOrWrapper(fieldClass)) {
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
            PathBuilder<Object> path = entityPath.get(criteria.getKey());
            return path.eq(criteria.getValue());
        }

        return null;
    }
}
