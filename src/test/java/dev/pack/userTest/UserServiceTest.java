package dev.pack.userTest;

import dev.pack.config.AppConfig;
import dev.pack.enums.Major;
import dev.pack.enums.Roles;
import dev.pack.exception.BadRequestException;
import dev.pack.modules.user.PagingRepository;
import dev.pack.modules.user.UserModel;
import dev.pack.modules.user.UserRepository;
import dev.pack.modules.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PagingRepository pagingRepository;

    @Mock
    private PasswordEncoder password;

    @InjectMocks
    private UserServiceImpl service;

    List<UserModel> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        UserModel user_1 = UserModel.builder()
                .id(1)
                .name("User Test 1")
                .username("Username Test 1")
                .email("UserTest1@gmail.com")
                .password("Testing123")
                .role(Roles.ADMIN_TKJ)
                .build();
        UserModel user_2 = UserModel.builder()
                .id(2)
                .name("User Test 2")
                .username("Username Test 2")
                .email("UserTest2@gmail.com")
                .password("Testing123")
                .role(Roles.ADMIN_TKR)
                .build();
        UserModel user_3 = UserModel.builder()
                .id(3)
                .name("User Test 3")
                .username("Username Test 3")
                .email("UserTest3@gmail.com")
                .password("Testing123")
                .role(Roles.ADMIN_TE)
                .build();

        users.addAll(Arrays.asList(user_1,user_2,user_3));
    }

    @Test
    void itShouldCreateBatch() {
        Mockito.when(userRepository.saveAll(users)).thenReturn(users);

        Iterable<UserModel> savedUsers = service.createBatch(users);

        assertThat(savedUsers).isNotEmpty();
    }

    @Test
    void itShouldGetAllUserWithPaginating() {
        Pageable pageable = Pageable.ofSize(2).withPage(0);

        Mockito.when(pagingRepository.findAll(pageable)).thenReturn(new PageImpl<>(users, pageable, users.size()));

        Page<UserModel> resultPage = (Page<UserModel>) service.getAllUser(pageable);

        assertEquals(users.size(), resultPage.getTotalElements());
        assertEquals(users.size(), resultPage.getContent().size());
    }

    @Test
    void itShouldCreateUser() {
        UserModel user1 = UserModel.builder()
                .id(1)
                .name("User Test 3")
                .username("Username Test 3")
                .email("UserTest3@gmail.com")
                .password("Testing123")
                .role(Roles.ADMIN_TE)
                .build();

        Mockito
                .when(userRepository.save(Mockito.any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.<UserModel>getArgument(0));

        UserModel savedUser = service.createUser(user1);

        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getJoinAt()).isNotNull();
    }

    //    @Test
//    @Disabled
//    void itShouldErrorWhenUserHave2SameRole() {
//
//        UserModel user_1 = UserModel.builder()
//                .id(1)
//                .name("User Test 1")
//                .username("Username Test 9")
//                .email("UserTest9@gmail.com")
//                .password("Testing123")
//                .role(Roles.ADMIN_TKJ)
//                .build();
//        UserModel user_2 = UserModel.builder()
//                .id(2)
//                .name("User Test 2")
//                .username("Username Test 6")
//                .email("UserTest6@gmail.com")
//                .password("Testing123")
//                .role(Roles.ADMIN_TKJ)
//                .build();
//
//        List<UserModel> users = new ArrayList<>(Arrays.asList(user_1, user_2));
//
//        Mockito.when(userRepository.saveAll(users)).thenReturn(users);
//
//        assertThrows(BadRequestException.class, () -> {
//            service.createBatch(users);
//        });
//
//    }
}
