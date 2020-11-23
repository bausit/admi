package org.bausit.admin;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.Member;
import org.bausit.admin.models.Skill;
import org.bausit.admin.repositories.MemberRepository;
import org.bausit.admin.repositories.SkillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        List<Skill> skills = getSkills();

        //preload some test users
        Arrays.stream(new String[]{"Wayne", "Long", "BigDog", "danny"})
            .map(name -> Member.builder()
                .englishName(name)
                .chineseName("名字")
                .gender(Member.Gender.M)
                .skills(skills)
                .build())
            .forEach(memberRepository::save);
    }

    private List<Skill> getSkills() {
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
}
