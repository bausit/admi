package org.bausit.admin.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bausit.admin.dtos.SecurityUser;
import org.bausit.admin.models.Participant;
import org.bausit.admin.repositories.MemberRepository;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ParticipantService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Participant participant = memberRepository.findByEmail(email).get(0);
        Hibernate.initialize(participant.getPermissions());
        return new SecurityUser(participant);
    }
}
