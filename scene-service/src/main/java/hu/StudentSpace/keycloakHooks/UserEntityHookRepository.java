package hu.StudentSpace.keycloakHooks;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityHookRepository extends JpaRepository<UserEntityHook, String> {
}
