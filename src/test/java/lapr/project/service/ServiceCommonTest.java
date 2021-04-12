package lapr.project.service;

import lapr.project.data.AddressDB;
import lapr.project.data.CommonDB;
import lapr.project.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceCommonTest {

    @Mock
    private CommonDB commonDB;
    @Mock
    private AddressDB addressDB;

    @InjectMocks
    private  ServiceCommon serviceCommon = new ServiceCommon();

    @BeforeEach
    void setUp() {
    }

    @Test
    void runScriptOnDatabase() throws SQLException, FileNotFoundException {
        when(commonDB.runScriptOnDatabase(anyString(), anyBoolean())).thenReturn(true);
        assertTrue(serviceCommon.runScriptOnDatabase("aaa", true));

        when(commonDB.runScriptOnDatabase(anyString(), anyBoolean())).thenReturn(false);
        assertFalse(serviceCommon.runScriptOnDatabase("aaa", false));

    }

    @Test
    void insertAddress() throws SQLException {
        Address address1 = new Address(3,20,-32, "rua", "2500", 100);

        when(addressDB.insertAddress(address1)).thenReturn(address1);
        Address result = serviceCommon.insertAddress(address1);
        assertEquals(address1, result);
        verify(addressDB).insertAddress(address1);
    }
}