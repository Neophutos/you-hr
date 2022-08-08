package com.example.application.data.service;

import com.example.application.data.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.application.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @desc UserService stellt den Dienst zur Erstellung, Veränderung und Löschung von Nutzern im System dar.
 *
 * @category Service
 * @version 1.0
 * @since 2022-08-05
 */
@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(UUID id) {
        return repository.findById(id);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<User> findAll(){
        return repository.findAll();
    }

}
