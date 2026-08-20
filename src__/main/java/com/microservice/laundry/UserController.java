package com.microservice.laundry;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/users")
public class UserController {

    private final LaundryUserRepository laundryUserRepository;

    public UserController(LaundryUserRepository laundryUserRepository) {
        this.laundryUserRepository = laundryUserRepository;
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable String id) {
        return "User: " + id;
    }

    @PostMapping
    public String createUser(@RequestBody String entity) {
        LaundryUser user = new LaundryUser();
        user.setName(entity);
        laundryUserRepository.save(user);
        return entity;
    }

    @GetMapping("/")
    public List<String> getUsers() {
        return laundryUserRepository.findAll()
                .stream()
                .map(LaundryUser::getName)
                .toList();
    }
    
    
}
