package org.bausit.admin;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.*;
import org.bausit.admin.repositories.*;
import org.bausit.admin.services.ParticipantService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ParticipantService participantService;
    private final SkillRepository skillRepository;
    private final TeamRepository functionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final EventRepository eventRepository;
    private final TeamMemberRepository fmRepository;

    @Override
    public void run(String... args) {
        Skill  chef = createSkill("Chef");
        Skill  engineering = createSkill("Engineering");

        Permission su = permissionRepository.save(Permission.builder()
            .name("super")
            .build());
        Permission participantRead = permissionRepository.save(Permission.builder()
            .name("participants_read")
            .build());
        Permission participantWrite = permissionRepository.save(Permission.builder()
            .name("participants_write")
            .build());
        Permission eventRead = permissionRepository.save(Permission.builder()
            .name("events_read")
            .build());
        Permission eventWrite = permissionRepository.save(Permission.builder()
            .name("events_write")
            .build());

        Set<Participant> participants = Arrays.stream(new String[]{"Wayne", "Long", "BigDog", "danny", "lee", "user"})
            .map(name -> Participant.builder()
                .firstName(name)
                .lastName("Admin")
                .chineseName("名字")
                .email(name + "@mail.com")
                .phoneNumber("555-123-0000")
                .address("address for " + name)
                .city("New York").zipcode("10001").password(passwordEncoder.encode("password"))
                .gender(Participant.Gender.M)
                .state("NY")
                .type(Participant.Type.V)
                .skills(List.of(chef,engineering))
                .birthYear(2000).issueDate(Instant.now())
                .permissions(name.equals("user") ? List.of(): List.of(su))
                .build())
            .map(participantService::create)
            .collect(Collectors.toSet());

        Event event = Event.builder().name("清明法会Chinese Lunar New Year Blessing Ceremony")
                .date(Instant.now().plus(Duration.ofDays(30)))
                .location("the Temple")
                //.invitedParticipants(participants)
                .build();
        event = eventRepository.save(event);

        Team cafe = createTeam("Food", event, List.of(chef));
        Team it = createTeam("IT", event, List.of(engineering));

        event.setTeams(List.of(cafe,it));
        eventRepository.save(event);

    }

    private Skill createSkill(String skill) {
        return skillRepository.save(
            Skill.builder().name(skill).description(skill)
             .build()
        );
    }

    private Team createTeam(String teamName, Event event, List<Skill> skills) {
        return  functionRepository.save(
                Team.builder().name(teamName).description(teamName).event(event).skills(skills).build()
            );
    }
}
