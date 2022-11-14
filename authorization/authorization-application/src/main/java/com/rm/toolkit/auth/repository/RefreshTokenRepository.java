package com.rm.toolkit.auth.repository;

import com.rm.toolkit.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByHash(String hash);

    @Modifying
    void deleteAllByExpiryDateBefore(Instant date);
}
