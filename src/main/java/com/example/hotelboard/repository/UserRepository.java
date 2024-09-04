package com.example.hotelboard.repository;

import com.example.hotelboard.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {

    Users findByUserIdAndPassword(String userId, String password);
    boolean existsByUserId(String userId); // New method to check if userId exists
    // String 타입 userId를 받는 커스텀 메소드
    @Query("SELECT u FROM Users u WHERE u.userId = :userId")
    Optional<Users> findByUserId(@Param("userId") String userId);

}
