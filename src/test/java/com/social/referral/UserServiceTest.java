package com.social.referral;

import com.social.referral.dto.UserDTO;
import com.social.referral.entities.Company;
import com.social.referral.entities.User;
import com.social.referral.repository.UserRepository;
import com.social.referral.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void testGetUserById_UserExists() {
        User responseUser = new User();
        responseUser.setName("TName");
        responseUser.setId(1);
        responseUser.setEmail("Test@mail");
        responseUser.setCompany(new Company(1,"IBM", new Date()));

        when(userRepository.findById(1)).thenReturn(Optional.of(responseUser));

        UserDTO user = userService.getUser(1);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getCompany());

        verify(userRepository, times(1)).findById(1);
        verifyNoMoreInteractions(userRepository);
    }

//    @Test
//    public void testGetUserById_UserNotFound() {
//        // Simulate a scenario where the user is not found
//        when(userRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Use assertThrows to test the expected exception
//        Assertions.assertThrows(new UsernameNotFoundException.class, () -> {userService.getUser(1);});
//
//        verify(userRepository, times(1)).findById(1);
//        verifyNoMoreInteractions(userRepository);
//    }
}







//    @Test
//    void checkUserCreation()
//    {
//        User
//        Assertions.assertNotNull(userService.);
//    }

