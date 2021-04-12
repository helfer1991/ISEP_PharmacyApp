package lapr.project.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author catarinaserrano
 */
public class UserRoleTest {
    
   
    UserRole instance1 = new UserRole("1", "Courier");
    UserRole instance2 = new UserRole("1", "Courier");
    UserRole instance3 = new UserRole("2", "Courier");
    UserRole instance4 = new UserRole("1", "Courier3");
    UserRole instance5= null;
    User user = new User("cat@gmail.com", "123");

    
    public UserRoleTest() {
        
    }

    /**
     * Tests de validation of arguments of the userRole constructor
     */
    @Test
    public void testNullParameters(){
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRole(null);
        });

         assertThrows(IllegalArgumentException.class, () -> {
            new UserRole("");
        });

         assertThrows(IllegalArgumentException.class, () -> {
            new UserRole("adminRole" ,null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRole("adminRole" ,"");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new UserRole(null ,"description");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new UserRole("" ,"description");
        });
    }

    /**
     * Test of getRole method, of class UserRole.
     */
    @Test
    public void testGetRole() {
        System.out.println("getRole");
        
        String expResult = "1";
        String result = instance1.getRole();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class UserRole.
     */
    @Test
    public void testGetDescription() {

        String expResult = "Courier";
        String result = instance1.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of hasId method, of class UserRole.
     */
    @Test
    public void testHasIdTrue() {
        String strId = "1";
        boolean expResult = true;
        boolean result = instance1.hasId(strId);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of hasId method, of class UserRole.
     */
    @Test
    public void testHasIdFalse() {
        String strId = "2";
        boolean expResult = false;
        boolean result = instance1.hasId(strId);
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class UserRole.
     */
    @Test
    public void testEquals() {

        assertEquals(instance1, instance1);
        assertEquals(instance1, instance2);
        assertNotEquals(instance1, instance3);
        assertNotEquals(instance1, instance4);
        assertNotEquals(instance1, instance5);
    }

    @Test
    void testToString() {
        assertEquals(instance1.toString(), instance1.toString());
    }

    @Test
    void testHashCode() {
        UserRole instance1 = new UserRole("1", "Courier");
        UserRole instance2 = new UserRole("1", "Courier");

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance2 = new UserRole("2", "Courier");
        assertNotEquals(instance1.hashCode(), instance2.hashCode());

        instance2 = new UserRole("1", "Courierasdfasdf");
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }
}
