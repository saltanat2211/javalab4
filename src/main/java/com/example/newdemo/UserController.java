package com.example.newdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;  // Внедряем репозиторий

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        userRepository.save(user);  // Сохраняем пользователя в базе данных
        return ResponseEntity.ok("Пользователь успешно добавлен");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
        user.setId(id);
        userRepository.save(user);  // Обновляем пользователя в базе данных
        return ResponseEntity.ok("Пользователь обновлен");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
        userRepository.deleteById(id);  // Удаляем пользователя
        return ResponseEntity.ok("Пользователь удален");
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());  // Получаем всех пользователей
    }
}
