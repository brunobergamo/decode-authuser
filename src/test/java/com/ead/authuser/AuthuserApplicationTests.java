package com.ead.authuser;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import com.ead.authuser.services.impl.UserServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthuserApplicationTests {

    @TestConfiguration
    static class UserServiceImplTesteContextConfiguration{

        @Bean
        public UserService userService(){
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        UserModel userModel = new UserModel();
        userModel.setFullName("Bruno Barreto Bergamo");
        userModel.setUsername("brunobergamo");
        Mockito.when(userRepository.existsByUsername(userModel.getUsername())).thenReturn(true);
        Mockito.when(userRepository.existsByUsername("bruno")).thenReturn(false);
    }

    @Test
    public void validateUserServiceExistsByUsername() {
        assertAll("teste", () -> assertEquals(userService.existsByUsername( "brunobergamo"),false),
                                    () -> assertEquals(userService.existsByUsername("bruno"),false));

        assertEquals(userService.existsByUsername( "brunobergamo"),false);
   //     assertThrows(Exception.class,() -> userService.existsByUsername( "brunobergamo"));
    }

}
