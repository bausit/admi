package org.bausit.admin.services;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RequiredArgsConstructor
@Service
public class ParticipantService implements UserDetailsService {
    private final ParticipantRepository participantRepository;

    public Iterable<Participant> query(String query) {
        PredicatesBuilder builder = new PredicatesBuilder(Participant.class);
        if (StringUtils.hasLength(query)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(query + ",");
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Participant participant = participantRepository.findByEmail(email).get(0);
        Hibernate.initialize(participant.getPermissions());
        return new SecurityUser(participant);
    }
}
