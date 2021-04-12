package lapr.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class UserTest {

    User user1 = new User("email", "string");
    User user2 = new User("email", "string2");
    User user3 = new User("email2", "string");
    
    private final UserRole role=null;
    User instance = new User("cat@gmail.com", "123");
    String email = "cat@gmail.com";
    
    public UserTest() {
    }
    

    /**
     * Test of setRole method, of class User.
     */
    @Test
    public void testSetRole() {
        boolean expResult = false;
        boolean result = instance.setRole(role);
        assertEquals(expResult, result);
    }

    /**
     * Test of getRole method, of class User.
     */
    @Test
    public void testHasEmail() {
        boolean expResult = true;
        boolean result = instance.hasEmail(email);
        assertEquals(expResult, result);

    }

    @Test
    void testHashCode() {

        assertNotEquals(user1.hashCode(), user2.hashCode());
        user2 = new User("email", "string");
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testEquals() {

        assertEquals(user1,user1);
        assertNotEquals(user1,user2);
        assertNotEquals(user1,user3);

        assertNotEquals(user1,null);
        assertNotEquals(user1,"String");

        user2 = new User("email", "string");
        user2.setRole(new UserRole("aa", "bb"));
        user1 = new User("email", "string");
        user1.setRole(new UserRole("aa", "bb"));
        assertEquals(user1, user2);

        user2.setRole(new UserRole("cc", "bb"));
        assertNotEquals(user1,user2);

    }

    @Test
    void invalidConstructorParameters(){
        assertThrows(IllegalArgumentException.class,()->{
            User user4 = new User("email",null);
        });
        assertThrows(IllegalArgumentException.class,()->{
            User user4 = new User(null,"string");
        });
        assertThrows(IllegalArgumentException.class,()->{
            User user4 = new User("email","");
        });
        assertThrows(IllegalArgumentException.class,()->{
            User user4 = new User("","string");
        });
    }


    @Test
    void hasEmail() {
        assertTrue(user1.hasEmail("email"));
        assertFalse(user1.hasEmail("wrongEmail"));
    }
}
