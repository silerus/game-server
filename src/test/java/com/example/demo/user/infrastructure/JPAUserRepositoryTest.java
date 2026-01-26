package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.Role;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.exception.UserAlreadyExistsException;
import com.example.demo.user.domain.exception.UserNotFoundException;
import com.example.demo.user.infrastructure.persistence.user.JpaUserRepository;
import com.example.demo.user.infrastructure.persistence.user.JpaUserRepositoryInterface;
import com.example.demo.user.infrastructure.persistence.user.UserMapper;
import com.example.demo.user.infrastructure.persistence.user.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserMapperImpl.class)
@ActiveProfiles("test")
class JPAUserRepositoryTest {

    @Autowired
    private JpaUserRepositoryInterface jpaRepo;

    @Autowired
    private UserMapper mapper;

    @Test
    void shouldSaveAndFindUser() throws UserAlreadyExistsException, UserNotFoundException {
        JpaUserRepository repository = new JpaUserRepository(jpaRepo, mapper);
        User user = new User(UUID.randomUUID(), "test@mail.com", "password", Role.USER);

        User saved = repository.save(user);
        User found = repository.findByEmail("test@mail.com");

        assertThat(found.getEmail()).isEqualTo("test@mail.com");
        assertThat(found.getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldThrowWhenDuplicateEmail() throws UserAlreadyExistsException {
        JpaUserRepository repository = new JpaUserRepository(jpaRepo, mapper);
        User user1 = new User(UUID.randomUUID(), "dup@mail.com", "password", Role.USER);
        User user2 = new User(UUID.randomUUID(), "dup@mail.com", "password2", Role.USER);

        repository.save(user1);
        assertThrows(UserAlreadyExistsException.class, () -> repository.save(user2));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        JpaUserRepository repository = new JpaUserRepository(jpaRepo, mapper);
        assertThrows(UserNotFoundException.class, () -> repository.findByEmail("notfound@mail.com"));
        assertThrows(UserNotFoundException.class, () -> repository.findById(UUID.randomUUID()));
    }
}
