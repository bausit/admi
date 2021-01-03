package org.bausit.admin;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.*;
import org.bausit.admin.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ParticipantRepository participantRepository;
    private final SkillRepository skillRepository;
    private final FunctionRepository functionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final EventRepository eventRepository;
    private final EventMemberRepository fmRepository;

    @Override
    public void run(String... args) {
        List<Skill> skills = createSkills();
        List<Team> teams = createFunctions();

        List<Event> activities = Arrays.stream(new String[] {"Chinese Lunar New Year Blessing Ceremony"})
            .map(name -> Event.builder()
                .name(name)
                .date(Instant.now())
                .teams(teams)
                .build())
            .map(eventRepository::save)
            .collect(Collectors.toList());

        Permission permission = permissionRepository.save(Permission.builder()
            .name("admin")
            .build());

        Arrays.stream(new String[]{"Wayne", "Long", "BigDog", "danny", "user"})
            .map(name -> Participant.builder()
                .englishName(name)
                .chineseName("名字")
                .email(name + "@mail.com")
                .password(passwordEncoder.encode("password"))
                .gender(Participant.Gender.M)
                .skills(skills)
                .issueDate(Instant.now())
                .permissions(name.equals("user") ? List.of(): List.of(permission))
                .build())
            .map(participantRepository::save)
            .map(member -> TeamMember.builder()
                .participant(member)
                .team(teams.get(0))
                .build()
            )
            .map(fmRepository::save)
            .collect(Collectors.toList());
    }

    private List<Skill> createSkills() {
        List<Skill> skills = new ArrayList<>();

        skills.add(skillRepository.save(
            Skill.builder().name("Chef")
             .build()
        ));
        skills.add(skillRepository.save(
            Skill.builder().name("Software Developer")
                .build()
        ));

        return skills;
    }

    private List<Team> createFunctions() {
        List<Team> teams = new ArrayList<>();
        teams.add(
            functionRepository.save(
                Team.builder().name("Organizer").build()
            )
        );
        teams.add(
            functionRepository.save(
                Team.builder().name("Leader").build()
            )
        );

        return teams;
    }
}
