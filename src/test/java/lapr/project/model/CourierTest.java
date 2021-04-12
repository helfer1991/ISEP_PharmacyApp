package lapr.project.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author catarinaserrano
 */
public class CourierTest {

    
    Courier courier1;
    Courier courier2;
    
    private final double weight =67.0;
    private Courier instance; 
    
    public CourierTest() {
        courier1 = new Courier(55, true);
        courier1.setNIF(259723444);
        courier1.setName("Catarina");
        courier1.setEmail("catarina@isep.ipp.pt");
        courier1.setPassword("catarina");
        courier2 = new Courier(55, true);
        courier2.setNIF(259723444);
        courier2.setName("Catarina");
        courier2.setEmail("catarina@isep.ipp.pt");
        courier2.setPassword("catarina");
        instance = new Courier(55, true);
        instance.setNIF(259723444);
        instance.setName("Catarina");
        instance.setEmail("catarina@isep.ipp.pt");
        instance.setPassword("catarina");
        
    }
    
    @Test
    public void testSetWeight() {
        double expResult =67.0;
        instance.setWeight(weight);
        double result = instance.getWeight();
        assertEquals(expResult, result);
    }

    @Test
    void getIsWorking() {
        Courier courier1 = new Courier(55, true);
        courier1.setNIF(259723444);
        courier1.setName("Catarina");
        courier1.setEmail("catarina@isep.ipp.pt");
        courier1.setPassword("catarina");
        assertTrue(courier1.getIsWorking());
        courier1.setIsWorking(false);
        assertFalse(courier1.getIsWorking());

    }

    @Test
    void testEquals() {
        assertNotEquals(courier1 ,null);
        assertNotEquals(courier1 ,"String");
        assertEquals(courier1 ,courier1);
        assertEquals(courier1 ,courier2);
        courier2 = new Courier(55, true);
        courier2.setNIF(259723444);
        courier2.setName("Catarina");
        courier2.setEmail("catarina@isep.ipp.pt");
        courier2.setPassword("catarina5");
        assertNotEquals(courier1 ,courier2);

    }

    @Test
    void testHashCode(){
        assertEquals(courier1.hashCode() ,courier1.hashCode());
        assertEquals(courier1.hashCode() ,courier2.hashCode());
        courier2 = new Courier(55, true);
        courier2.setNIF(259723444);
        courier2.setName("Catarina");
        courier2.setEmail("catarina@isep.ipp.pt");
        courier2.setPassword("catarina5");
        assertNotEquals(courier1.hashCode() ,courier2.hashCode());
    }
}
