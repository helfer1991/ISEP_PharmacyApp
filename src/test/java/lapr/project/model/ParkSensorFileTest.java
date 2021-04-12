package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkSensorFileTest {

    ParkSensorFile pk1 = new ParkSensorFile(1, 2,3, 4,"aa");
    ParkSensorFile pk2 = new ParkSensorFile(1, 2,3, 4,"aa");

    @Test
    void getScooterBatteryPercentage() {
        assertEquals(3, pk1.getVehicleBatteryPercentage());
    }

    @Test
    void getEstimatedTimeToFullCharge() {
        assertEquals(4, pk1.getEstimatedTimeToFullCharge());

    }

    @Test
    void testEquals() {
        assertNotEquals(pk1, null);
        assertNotEquals(pk1, new Battery(1, 2));

        assertEquals(pk1, pk1);
        assertEquals(pk1, pk2);

        pk2 = new ParkSensorFile(11,2,3,4,"aa");
        assertNotEquals(pk1,pk2);
        pk2 = new ParkSensorFile(1,22,3,4,"aa");
        assertNotEquals(pk1,pk2);
        pk2 = new ParkSensorFile(1,2,33,4,"aa");
        assertNotEquals(pk1,pk2);
        pk2 = new ParkSensorFile(1,2,3,44,"aa");
        assertNotEquals(pk1,pk2);
        pk2 = new ParkSensorFile(1,2,3,4,"bb");
        assertNotEquals(pk1,pk2);

    }

    @Test
    void testHashCode() {

        assertEquals(pk1.hashCode(), pk1.hashCode());
        assertEquals(pk1.hashCode(), pk2.hashCode());

        pk2 = new ParkSensorFile(66, 2,3, 4,"aa");
        assertNotEquals(pk1.hashCode(), pk2.hashCode());

        pk2 = new ParkSensorFile(1, 55,3, 4,"aa");
        assertNotEquals(pk1.hashCode(), pk2.hashCode());

        pk2 = new ParkSensorFile(1, 2,3, 4,"aaa");
        assertNotEquals(pk1.hashCode(), pk2.hashCode());

    }


}