package ru.clevertec.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
