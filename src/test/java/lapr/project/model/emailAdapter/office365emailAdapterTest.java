package lapr.project.model.emailAdapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import lapr.project.model.ServiceEmail;

class office365emailAdapterTest {

    ServiceEmail officeAdapter = new office365emailAdapter();

    @Test
    void sendEmail() {
        assertTrue(officeAdapter.sendEmail(
                "lapr3g19@outlook.pt",
                "application testing",
                "email generated while performing unit tests"));

    }
}