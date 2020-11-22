package org.bausit.admin;

import lombok.RequiredArgsConstructor;
import org.bausit.admin.models.User;
import org.bausit.admin.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        //preload some test users
        Arrays.stream(new String[]{"Wayne", "Long", "BigDog", "danny"})
            .map(name -> User.builder().firstName(name).build())
            .forEach(userRepository::save);
    }
}
