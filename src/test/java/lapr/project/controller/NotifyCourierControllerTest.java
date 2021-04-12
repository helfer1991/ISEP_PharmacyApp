package lapr.project.controller;

import lapr.project.data.CourierDB;
import lapr.project.data.VehicleDB;
import lapr.project.model.ParkSensorFile;
import lapr.project.service.ServiceParkSystemFiles;
import lapr.project.service.ServiceVehicle;
import lapr.project.utils.Constants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import lapr.project.model.ServiceEmail;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotifyCourierControllerTest {



    @Mock private Logger logger;
    @Spy ServiceParkSystemFiles srvParkFiles;
    @Mock
    ServiceVehicle srvScooter;
    @Mock private ServiceEmail emailService;
    @Mock private VehicleDB vehicleDB;
    @Mock private CourierDB courierDB;
    @InjectMocks private NotifyCourierController controllerInstance;



    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        try{
            //all serviceParkSystemFiles methods set to pass by default
            when(srvParkFiles.validateDetectedFile(anyString())).thenReturn(true);
            when(srvParkFiles.checkIfDataFileIsPresent(anyString())).thenReturn(true);
            doReturn(mock(ParkSensorFile.class)).when(srvParkFiles).readDataFile(anyString());
            when(srvParkFiles.validateParkSensorFile(any(ParkSensorFile.class))).thenReturn(true);
            when(srvParkFiles.deleteFileInInputDirectory(anyString())).thenReturn(true);


            //simulateScooterCharging set to pass by defaulf
            when(srvScooter.simulateVehicleCharging(any(ParkSensorFile.class))).thenReturn(true);
            //doReturn(true).when(srvScooter).simulateScooterCharging(any(ParkSensorFile.class));

            //all DB access set to pass by default
            when(vehicleDB.updateScooterStatus(any(ParkSensorFile.class), anyInt())).thenReturn(true);
            when(courierDB.getCourierEmailByVehicleId(anyInt())).thenReturn("email@fakeEmail.com");

            //sendEmail set to pass by default
            when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);

        }catch (Exception e){
            e.printStackTrace();
            fail("NotifyCourierControllerTest: Exception was thrown while stubbing");
        }
        doNothing().when(logger).log(any(Level.class), anyString(), any(Exception.class));
    }



    @Test
    void testStartWatch() {

        //confirming thread is not started before calling the method
        Thread thread = controllerInstance.getWatchFolderThread();
        Assertions.assertNotNull(thread);
        assertFalse( thread.isAlive() );


        //start the thread
        controllerInstance.startWatch();

        //confirming thread is started after calling the method
        thread = controllerInstance.getWatchFolderThread();
        Assertions.assertNotNull(thread);
        assertTrue( thread.isAlive() );


        //interrupt thread (InterruptException is caught)
        thread.interrupt();
    }



    @Test
    void testProcessDetectedFile_incorrectFlagName() {

        //ProcessDetectedFile returns false when the fileName is considered incorrect
        String flagFileName = "invalidflagFileName";
        when(srvParkFiles.validateDetectedFile(flagFileName)).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName) );

        //ProcessDetectedFile returns true when the fileName is considered incorrect
        flagFileName = "validflagFileName";
        when(srvParkFiles.validateDetectedFile(flagFileName)).thenReturn(true);
        //assertFalse(controllerInstance.processDetectedFile(flagFileName) ); //-------------

        //checks if the method was called with the flagFileName
        //verify(srvParkFiles).validateDetectedFile(flagFileName);
    }

    @Test
    void testProcessDetectedFile_dataFileNotPresent() {

        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

        //ProcessDetectedFile returns false if the data file is not present
        when(srvParkFiles.checkIfDataFileIsPresent(anyString())).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName));

        //ProcessDetectedFile returns true if the data file is present
        when(srvParkFiles.checkIfDataFileIsPresent(anyString())).thenReturn(true);
        //assertFalse(controllerInstance.processDetectedFile(flagFileName)); //-----
    }

    @Test
    void testProcessDetectedFile_incorrectRead() {

        try{
            String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

            //ProcessDetectedFile returns false if the data file can't be read
            doReturn(null).when(srvParkFiles).readDataFile(anyString());
            assertFalse(controllerInstance.processDetectedFile(flagFileName));

            //ProcessDetectedFile returns true if the data file was correctly read
            doReturn(mock(ParkSensorFile.class)).when(srvParkFiles).readDataFile(anyString());
            //assertTrue(controllerInstance.processDetectedFile(flagFileName));

        }catch (Exception e){
            fail();
        }

    }

    @Test
    void testProcessDetectedFile_invalidFileContent() {

        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

        //ProcessDetectedFile returns false if the estimate.data file content is not validated
        when(srvParkFiles.validateParkSensorFile(any(ParkSensorFile.class))).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName));

        //ProcessDetectedFile returns false if the estimate.data file content is correctly validated
        when(srvParkFiles.validateParkSensorFile(any(ParkSensorFile.class))).thenReturn(true);
        //assertFalse(controllerInstance.processDetectedFile(flagFileName)); //---------

    }
/*
    @Test
    void testProcessDetectedFile_deleteFileFail() {

        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

        //ProcessDetectedFile returns false if the files are not deleted
        when(srvParkFiles.deleteFileInInputDirectory(anyString())).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName));

        //ProcessDetectedFile returns true if the files are deleted
        when(srvParkFiles.deleteFileInInputDirectory(anyString())).thenReturn(true);
        assertTrue(controllerInstance.processDetectedFile(flagFileName));

    }

    @Test
    void testProcessDetectedFile_updateScooterFail() {


        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";
        try{
            //ProcessDetectedFile returns false if the scooterStatus is not updated
            when(vehicleDB.updateScooterStatus(any(ParkSensorFile.class), anyInt())).thenReturn(false);
            assertFalse(controllerInstance.processDetectedFile(flagFileName));

            //ProcessDetectedFile returns false if the scooterStatus is updated correctly
            when(vehicleDB.updateScooterStatus(any(ParkSensorFile.class), anyInt())).thenReturn(true);
            assertTrue(controllerInstance.processDetectedFile(flagFileName));

        }catch (Exception e){
            fail("Failed test: testProcessDetectedFile_updateScooterFail");
        }

    }

    @Test
    void testProcessDetectedFile_chargingFail() {

        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

        //ProcessDetectedFile returns false if the charging timer is not set
        when(srvScooter.simulateVehicleCharging(any(ParkSensorFile.class))).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName));

        //ProcessDetectedFile returns true if the charging timer is set up correctly
        when(srvScooter.simulateVehicleCharging(any(ParkSensorFile.class))).thenReturn(true);
        assertTrue(controllerInstance.processDetectedFile(flagFileName));
    }

    @Test
    void testProcessDetectedFile_sendEmailFail() {

        String flagFileName = "estimate_2018_01_01_23_12_30.data.flag";

        //ProcessDetectedFile returns false if the email is not sent
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(false);
        assertFalse(controllerInstance.processDetectedFile(flagFileName));

        //ProcessDetectedFile returns true if the email is sent correctly
        when(emailService.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
        assertTrue(controllerInstance.processDetectedFile(flagFileName));
    }

    @Test
    void testProcessDetectedFile_Pass() {

        try{
            //all serviceParkSystemFiles methods set to call real method
            when(srvParkFiles.validateDetectedFile(anyString())).thenCallRealMethod();
            when(srvParkFiles.deleteFileInInputDirectory(anyString())).thenCallRealMethod();
            when(srvParkFiles.checkIfDataFileIsPresent(anyString())).thenCallRealMethod();
            when(srvParkFiles.readDataFile(anyString())).thenCallRealMethod();

        }catch (Exception e){
            fail("exception was thrown while stubbing");
        }

        String flagFileName = "estimate_1994_12_31_12_00_00.data.flag";
        String dataFileName = "estimate_1994_12_31_12_00_00.data";
        File flagFile = null;
        File dataFile = null;
        try {
            flagFile = new File(Constants.INPUT_FILES_DIRECTORY + flagFileName);
            flagFile.createNewFile();
            dataFile = new File(Constants.INPUT_FILES_DIRECTORY + dataFileName);
            PrintWriter writer = new PrintWriter(dataFile);
            writer.println("ScooterId: 1");
            writer.println("ScooterBatteryCapacity: 187");
            writer.println("ScooterBatteryPercentage: 10");
            writer.println("EstimatedTimeToFullCharge(min): 187.50");
            writer.println("Reestimate: 1");
            writer.close();

            boolean expectedResult = true;
            boolean result = controllerInstance.processDetectedFile(flagFileName);

            Assertions.assertEquals(expectedResult, result);

        } catch (IOException e) {
            //fails test if exception is thrown
            Assertions.assertEquals(false, true);
        }finally{
            if(dataFile != null)
                flagFile.delete();
            if(dataFile != null)
                dataFile.delete();
        }

    }

*/

}