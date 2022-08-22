package com.twitagram.server.utils.jwt;

import com.twitagram.server.entity.Member;
import com.twitagram.server.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
