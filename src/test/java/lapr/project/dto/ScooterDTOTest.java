package lapr.project.dto;

import lapr.project.ui.dto.BatteryDTO;
import lapr.project.ui.dto.ScooterDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Asus
 */
public class ScooterDTOTest {

    ScooterDTO scooter;


    @BeforeEach
    void setUp() {
        scooter = new ScooterDTO(13,55564, 450, new BatteryDTO(1,150),85);
    }


    @Test
    void getIdScooter() {
        assertEquals(13, scooter.getId());
    }

    @Test
    void setId_Scooter() {
        scooter.setId(22);
        assertEquals(22, scooter.getId());
    }

    @Test
    void getQrCode() {
        assertEquals(55564, scooter.getQrCode());

    }

    @Test
    void setQrCode() {
        scooter.setQrCode(333);
        assertEquals(333, scooter.getQrCode());
    }

    @Test
    void getIsAvailable() {
        assertEquals("true", scooter.getIsAvailable());
    }

    @Test
    void setIsAvailable() {
        scooter.setIsAvailable("abc");
        assertEquals("abc", scooter.getIsAvailable());
    }


    @Test
    void getWeight() {
        assertEquals(450, scooter.getWeight());
    }

    @Test
    void setWeight() {
        scooter.setWeight(111);
        assertEquals(111, scooter.getWeight());
    }

    @Test
    void getBattery() {
        assertEquals(new BatteryDTO(1,150), scooter.getBattery());
    }

    @Test
    void setBattery() {
        scooter.setBattery(new BatteryDTO(1, 2));
        assertEquals(new BatteryDTO(1,2), scooter.getBattery());
    }

    @Test
    void testEquals() {

        assertFalse(scooter.equals(null));
        assertFalse(scooter.equals(new BatteryDTO(4, 55)));

        ScooterDTO scooter1 = new ScooterDTO(1,2,3,new BatteryDTO(4,5),90);
        ScooterDTO scooter2 = new ScooterDTO(1,2,3,new BatteryDTO(4,5),90);
        assertTrue(scooter1.equals(scooter2));

        ScooterDTO scooter3 = new ScooterDTO(1,2,3,new BatteryDTO(4, 5),90);
        assertTrue(scooter2.equals(scooter3));

    }
}
