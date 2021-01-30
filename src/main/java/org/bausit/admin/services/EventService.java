package org.bausit.admin.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.Event;
import org.bausit.admin.models.QEvent;
import org.bausit.admin.repositories.EventRepository;
import org.bausit.admin.search.PredicatesBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Iterable<Event> query(String query) {
        PredicatesBuilder builder = new PredicatesBuilder(Event.class);
        QEvent event = QEvent.event;

        if (StringUtils.hasLength(query)) {
            Matcher matcher = PredicatesBuilder.PATTERN.matcher(query + ",");
            while (matcher.find()) {
                Object value = matcher.group(3);
                String name = matcher.group(1);
                builder.with(name, matcher.group(2), value);
            }
        }
        BooleanExpression exp = builder.build();
        return eventRepository.findAll(exp);
    }
}
