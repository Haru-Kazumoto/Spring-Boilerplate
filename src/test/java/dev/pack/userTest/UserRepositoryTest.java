package dev.pack.userTest;

import dev.pack.enums.Major;
import dev.pack.enums.Roles;
import dev.pack.modules.user.UserModel;
import dev.pack.modules.user.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void itShouldCreateUser() {
        UserModel user = UserModel.builder()
                .id(1)
                .name("Testing User")
                .username("TestingUser")
                .email("TestingUser@gmail.com")
                .password("123456")
                .role(Roles.ADMIN_TKJ)
                .build();

        UserModel savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getJoinAt()).isNotNull();
    }

    @Test
    void itShouldErrorWhenEmailPatternDoesntValid() {
        UserModel user = UserModel.builder()
                .id(1)
                .name("Testing User")
                .username("TestingUser")
                .email("TestingUsergmail.com")
                .password("123456")
                .role(Roles.ADMIN_TKJ)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });
    }
}
