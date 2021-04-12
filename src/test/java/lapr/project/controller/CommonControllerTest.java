package lapr.project.controller;

import lapr.project.data.AddressDB;
import lapr.project.data.CommonDB;
import lapr.project.data.RoadDB;
import lapr.project.model.Address;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Road;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.RoadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommonControllerTest {

    @Mock
    private AddressDB addressDB;

    @Mock
    private CommonDB commonDB;

    @Mock
    private RoadDB roadDB;

    private CommonController commonController;


    @BeforeEach
    void setUp() {
        commonController= new CommonController();
    }

    @Test
    void runScriptOnDatabase() throws FileNotFoundException, SQLException {
        PharmaDeliveriesApp.getInstance().getServiceCommon().setCommonDB(commonDB);

        when(commonDB.runScriptOnDatabase(anyString(), anyBoolean())).thenReturn(true);
        assertTrue(commonController.runScriptOnDatabase("aaa", true));

        when(commonDB.runScriptOnDatabase(anyString(), anyBoolean())).thenReturn(false);
        assertFalse(commonController.runScriptOnDatabase("aaa", true));

    }

    @Test
    void getAllAddress() {
        assertNotNull(commonController.getAllAddress());
    }

    @Test
    void insertAddress() throws SQLException {
        PharmaDeliveriesApp.getInstance().getServiceCommon().setAddressDB(addressDB);

        Address address1 = new Address( 3,20,-32, "rua", "2500", 100);
        AddressDTO expectedResult = DTOConverter.convertAddress(address1);

        when(addressDB.insertAddress(address1)).thenReturn(address1);
        AddressDTO result = commonController.insertAddress(expectedResult);
        assertEquals(expectedResult, result);
        verify(addressDB).insertAddress(address1);
    }

    @Test
    void insertRoad() throws SQLException {
        Address address1 = new Address( 3,20,-32, "rua", "2500", 100);
        Address address2 = new Address( 4,20,-32, "rua", "2500", 100);

        Road road1 = new Road(5, address1, address2);

        PharmaDeliveriesApp.getInstance().getServiceCommon().setRoadDB(roadDB);

        when(roadDB.insertRoadRestriction(any(Road.class))).thenReturn(road1);

        RoadDTO expectedResult = DTOConverter.convertRoad(road1);

        RoadDTO result = commonController.insertRoadRestriction(expectedResult);

        assertEquals(expectedResult,result);
    }
}