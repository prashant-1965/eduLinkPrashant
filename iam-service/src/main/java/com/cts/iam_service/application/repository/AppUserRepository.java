package com.cts.iam_service.application.repository;

import com.cts.iam_service.application.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    @Query("select a from AppUser a where a.userEmail = :userEmail")
    Optional<AppUser> findAppUserByUserEmail(@Param("userEmail") String userEmail);

    @Query("select a from AppUser a where a.phoneNumber = :phoneNumber")
    Optional<AppUser> findAppUserByUserPhoneNumber(@Param("phoneNumber") Long phoneNumber);

    @Query("select a from AppUser a where a.userEmail = :userEmail")
    Optional<AppUser> findByUserEmail(@Param("userEmail") String userEmail);
}

