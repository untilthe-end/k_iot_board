package org.example.boardback.repository.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.example.boardback.common.enums.AuthProvider;
import org.example.boardback.entity.user.User;
import org.example.boardback.security.user.UserPrincipalMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(@NotBlank(message = "이메일은 필수입니다.") @Email(message = "이메일 형식이 올바르지 않습니다.") String email);

    // OAuth2용
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    // username으로 조회하면서 userRoles + role까지 한 번에 패치 조인
    @Query("""
        select distinct u
        from User u
        left join fetch u.userRoles ur
            left join fetch ur.role r
        where u.username = :username
    """)
    Optional<User> findWithRolesByUsername(@Param("username") String username);
}