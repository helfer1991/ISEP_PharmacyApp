package lapr.project.model.autorization;

import lapr.project.model.User;
import lapr.project.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserSessionTest {


    User user1;
    UserRole userRole1;
    UserSession userSession1;
    @BeforeEach
    void setUp() {
        user1 = new User("fakeEmail", "pwd");
        userSession1 = new UserSession(user1);
        userRole1 = new UserRole("111", "role");
        user1.setRole(userRole1);
    }

    @Test
    void constructorTes(){

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            UserSession userSession = new UserSession(null);
        });
        assertEquals("Argument cannot be null.", exception.getMessage());

    }

    @Test
    void getUser() {
        assertEquals(user1, userSession1.getUser());
    }


    @Test
    void isLoggedIn() {
        assertTrue(userSession1.isLoggedIn());
    }

    @Test
    void getUserEmail() {
        assertEquals("fakeEmail", userSession1.getUserEmail());
        userSession1.doLogout();
        assertNull(userSession1.getUserEmail());
    }

    @Test
    void getUserRole() {
        assertEquals(userRole1, userSession1.getUserRole());
    }

    @Test
    void hasRole() {
        UserRole userRole2 = new UserRole("222", "role2");
        assertFalse(userSession1.hasRole(userRole2));
        assertTrue(userSession1.hasRole(userRole1));
        userSession1.doLogout();
        assertFalse(userSession1.hasRole(userRole1));
    }
}