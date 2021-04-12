/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author catarinaserrano
 */
public class PersonTest {

    private Person instance; 
    private Person instanceAlt; 
    private Person instanceAlt1; 
    private Person instanceAlt2; 
    private Person instanceAlt3; 
    private Person instanceAlt4;

    private final Person instanceNull = null;
    User user = new User("cat@gmail.com", "123");
    private final int nif = 111222333;
    private final String name = "antonio";

    public PersonTest() {
        
    instance = new Person(123456765, "catarina");
    instance.setEmail("catarina@isep.ipp.pt");
    instance.setPassword("123");
    instanceAlt = new Person(123456765, "catarina");
    instanceAlt.setEmail("catarina@isep.ipp.pt");
    instanceAlt.setPassword("123");
    instanceAlt1 = new Person(1234567655, "catarina");
    instanceAlt1.setEmail("catarina@isep.ipp.pt");
    instanceAlt1.setPassword("123");
    instanceAlt2 = new Person(123456765, "catarinaaa");
    instanceAlt2.setEmail("catarina@isep.ipp.pt");
    instanceAlt2.setPassword("123");
    instanceAlt3 = new Person(123456765, "catarina");
    instanceAlt3.setEmail("catarina@isep.ipp.pttt");
    instanceAlt3.setPassword("123");
    instanceAlt4 = new Person(123456765, "catarina");
    instanceAlt4.setEmail("catarina@isep.ipp.pt");
    instanceAlt4.setPassword("12344");
}

/**
 * Test of setNIF method, of class Person.
 */
@Test
        public void testSetNif() {

        instance.setNIF(nif);
        assertEquals(nif, instance.getNIF());

    }

    /**
     * Test of setName method, of class Person.
     */
    @Test
        public void testSetName() {
        
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     * Test of hashCode method, of class Person.
     */
    @Test
        public void testHashCode() {

        assertEquals(instanceAlt.hashCode(), instance.hashCode());
    }

    /**
     * Test of equals method, of class Person.
     */
    @Test
        public void testEquals() {

        assertEquals(instance, instance);
        assertEquals(instance, instanceAlt);
        assertNotEquals(instance, instanceAlt1);
        assertNotEquals(instance, instanceAlt2);
        assertNotEquals(instance, instanceAlt3);
        assertNotEquals(instance, instanceAlt4);
        assertNotEquals(instance, user);
        assertNotEquals(instance, instanceNull);

    }
    
}
