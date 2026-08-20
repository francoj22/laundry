package com.microservice.laundry;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(LaundryUserRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<String> defaultUsers = List.of("john", "maria", "alex");
                defaultUsers.forEach(name -> {
                    LaundryUser user = new LaundryUser();
                    user.setName(name);
                    repository.save(user);
                });
            }
        };
    }
}
