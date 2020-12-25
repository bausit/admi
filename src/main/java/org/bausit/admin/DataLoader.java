package org.bausit.admin;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.*;
import org.bausit.admin.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final FunctionRepository functionRepository;
    private final ActivityRepository activityRepository;
    private final ActivityMemberRepository amRepository;

    @Override
    public void run(String... args) {
        List<Skill> skills = createSkills();
        List<Function> functions = createFunctions();

        List<Activity> activities = Arrays.stream(new String[] {"Chinese Lunar New Year Blessing Ceremony", "Qinming Ceremony"})
            .map(name -> Activity.builder()
                .name(name)
                .date(Instant.now())
                .build())
            .map(activityRepository::save)
            .collect(Collectors.toList());

        Arrays.stream(new String[]{"Wayne", "Long", "BigDog", "danny"})
            .map(name -> Member.builder()
                .englishName(name)
                .chineseName("名字")
                .gender(Member.Gender.M)
                .skills(skills)
                .issueDate(Instant.now())
                .build())
            .map(memberRepository::save)
            .map(member -> ActivityMember.builder()
                .activity(activities.get(0))
                .member(member)
                .function(functions.get(0))
                .build()
            )
            .map(amRepository::save)
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

    private List<Function> createFunctions() {
        List<Function> functions = new ArrayList<>();
        functions.add(
            functionRepository.save(
                Function.builder().name("Organizer").build()
            )
        );
        functions.add(
            functionRepository.save(
                Function.builder().name("Leader").build()
            )
        );

        return functions;
    }
}
