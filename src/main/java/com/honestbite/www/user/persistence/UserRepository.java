package com.honestbite.www.user.persistence;

import com.honestbite.www.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}
