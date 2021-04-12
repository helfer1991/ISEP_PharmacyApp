package lapr.project.model;

import org.junit.jupiter.api.Test;

import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;

class PharmaDeliveriesAppTest {

    private PharmaDeliveriesApp instance = PharmaDeliveriesApp.getInstance();

    @Test
    void getAddressGraph() {
        assertNotNull(instance.getAerialGraph());
    }


    @Test
    void doLogin() {
        assertTrue(instance.doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY));
        assertFalse(instance.doLogin("email", "wrong"));
    }

}