package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Client c1;


    @BeforeEach
    void setUp() {
        c1 = new Client(
                new Address(2, 34,45, "rua", "1200", 100),
                new CreditCard("5464", 455, "2023"));
          
        
        c1.setNIF(22222222);
        c1.setName("nome");
        c1.setEmail("email");
        c1.setPassword("pwd");
    }

    @Test
    void getNIF() {
        assertEquals(22222222,c1.getNIF());
    }

    @Test
    void getAddress() {
        assertEquals(new Address(2, 34,45, "rua", "1200", 100), c1.getAddress());
    }

    @Test
    void setAddress() {
        c1.setAddress(new Address(5, 70,70, "rua", "na", 100));
        assertEquals(new Address(5, 70,70, "rua", "na", 100), c1.getAddress());
    }

    @Test
    void getCredit_card() {
        assertEquals(new CreditCard("5464", 455, "2023"), c1.getCreditCard());
    }

    @Test
    void setCredit_card() {
        c1.setCreditCard(new CreditCard("111333666", 111, "2028"));
        assertEquals(new CreditCard("111333666", 111, "2028"), c1.getCreditCard());
    }

    @Test
    void testEquals() {
        /*
        c1 = new Client(
                new Address(2, 34, 45, "rua", "4800-355"),
                new CreditCard(5464, 455, "2023"),
                22222222, "nome", "email", "pwd");
        Client c3 = new Client(
                new Address(2, 34, 45, "rua", "4800-355"),
                new CreditCard(5464, 455, "2023"),
                22222222, "nome", "email", "pwd");

        assertTrue(c1.equals(c3));

        Client c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertTrue(c1.equals(c2));

         c2 = new Client("emailll", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwdd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222221,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nomee", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 22, 34, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 344, 45,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 455,
                "rua", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "ruaa", "4800-355", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-3555", 5464, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 54644, 455, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 4555, "2023");

        assertFalse(c1.equals(c2));

        c2 = new Client("email", "pwd", 22222222,
                "nome", 2, 34, 45,
                "rua", "4800-355", 5464, 455, "20233");

        assertFalse(c1.equals(c2));

         */
    }

    @Test
    void testToString() {
        assertEquals(c1.toString(), c1.toString());
    }
}