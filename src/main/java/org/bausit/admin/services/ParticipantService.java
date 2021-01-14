package org.bausit.admin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.models.Participant;
import org.bausit.admin.repositories.ParticipantRepository;
import org.bausit.admin.search.PredicatesBuilder;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

@Log4j2
@RequiredArgsConstructor
@Service
public class ParticipantService implements UserDetailsService {
    private final ParticipantRepository participantRepository;
    private final ObjectMapper objectMapper;

    public Participant create(Participant participant) {
        participant.setEmail(participant.getEmail().toLowerCase());
        return participantRepository.save(participant);
    }

    public Iterable<Participant> query(String query) {
        PredicatesBuilder builder = new PredicatesBuilder(Participant.class);
        if (StringUtils.hasLength(query)) {
            Matcher matcher = PredicatesBuilder.PATTERN.matcher(query + ",");
            while (matcher.find()) {
                Object value = matcher.group(3);
                String name = matcher.group(1);

                if("gender".equals(name)) {
                    value = Participant.Gender.valueOf(matcher.group(3));
                }
                else if("type".equals(name)) {
                    value = Participant.Type.valueOf(matcher.group(3));
                }
                builder.with(name, matcher.group(2), value);
            }
        }
        BooleanExpression exp = builder.build();
        return participantRepository.findAll(exp);
    }

    public Map getPreference(Participant participant, String section) {
        Map preferences = new HashMap();
        try {
            if(StringUtils.hasLength(participant.getPreferences()))
                preferences = objectMapper.readValue(participant.getPreferences(), Map.class);
        } catch (JsonProcessingException e) {
            log.info("failed to parse participant preference into map, {}", participant.getPreferences());
        }

        if(preferences.containsKey(section)) {
            return (Map)preferences.get(section);
        }

        return new HashMap();
    }

    public boolean updatePreference(Participant participant, String section, Map<String, Object> preference) {
        Map preferences = new HashMap();
        try {
            if(StringUtils.hasLength(participant.getPreferences()))
                preferences = objectMapper.readValue(participant.getPreferences(), Map.class);
        } catch (JsonProcessingException e) {
            log.info("failed to parse participant preference into map, {}", participant.getPreferences());
        }

        String existingPreferences = participant.getPreferences();
        preferences.put(section, preference);
        log.info("updated preferences: {}", preferences);
        try {
            participant.setPreferences(objectMapper.writeValueAsString(preferences));
            participantRepository.save(participant);
        } catch (JsonProcessingException e) {
            log.info("failed to convert participant preference into string, {}", preference);
            participant.setPreferences(existingPreferences);
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Participant participant = participantRepository.findByEmail(email.toLowerCase())
            .get(0);
        Hibernate.initialize(participant.getPermissions());
        return new SecurityUser(participant);
    }
}
