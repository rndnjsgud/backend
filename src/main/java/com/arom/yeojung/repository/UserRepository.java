package com.arom.yeojung.repository;

import com.arom.yeojung.object.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserId(Long userId);

  Optional<User> findByUsername(String username);

  Boolean existsByNickname(String nickname);

  Boolean existsByUsername(String username);
}
