package lapr.project.service;

import lapr.project.model.ParkSensorFile;
import lapr.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ServiceParkSystemFilesTest {

    private ServiceParkSystemFiles serviceInstance;


    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        serviceInstance = new ServiceParkSystemFiles();
    }




    @ParameterizedTest
    @MethodSource("generateFileNames")
    void testValidateDetectedFile(String fileName, boolean expectedResult) {

        boolean result = serviceInstance.validateDetectedFile(fileName);
        assertEquals(expectedResult, result);
    }
    private static Stream<Arguments> generateFileNames() {
        return Stream.of(
                Arguments.of(null, false),
                Arguments.of("  ", false),
                Arguments.of("blank", false),
                Arguments.of("estimate_[datetime].flag", false),
                Arguments.of("estimate_2018_01_01_23_12_30.data.flag", true),
                Arguments.of("estimate_1100_11_06_11_59_59.data.flag", true),
                Arguments.of(" estimate_2018_01_01_23_12_30.data.flag", false),
                Arguments.of("estimate_2018_01_01_23_12_30.data.flag ", false),
                Arguments.of("estimate_2018_01_01_23_12_30.data.", false),
                Arguments.of("lock_2018_01_01_23_12_30.data.flag", false),
                Arguments.of("lock_2018_01_01_23_12_30.data.", false)
        );
    }



    @Test
    void testCheckIfDataFileIsPresent() {

        String dataFileName = "estimate_2018_01_01_23_12_30.data";
        String diferentName = "estimate_2018_23_23_23_23_30.data";
        File dataFile = null;
        File diferentFile = null;
        try {

            dataFile = new File(Constants.INPUT_FILES_DIRECTORY + dataFileName);
            dataFile.createNewFile();

            //data file exists
            assertTrue( serviceInstance.checkIfDataFileIsPresent(dataFileName) );

            //data file does not exist
            dataFile.delete();
            assertFalse( serviceInstance.checkIfDataFileIsPresent(dataFileName) );


            diferentFile = new File(Constants.INPUT_FILES_DIRECTORY + diferentName);
            diferentFile.createNewFile();

            assertFalse( serviceInstance.checkIfDataFileIsPresent(dataFileName) );

        } catch (IOException e) {
            fail("exception thrown while testing estimate data file");
        } finally{
            if(dataFile != null)
                dataFile.delete();
            if(diferentFile != null)
                diferentFile.delete();
        }
    }

    @Test
    void testReadDataFile() {
        String dataFileName = "estimate_2018_01_01_23_12_30.data";
        File dataFile = null;
        try {

            dataFile = new File(Constants.INPUT_FILES_DIRECTORY + dataFileName);
            dataFile.createNewFile();

            //if dataFile is empty should return null
            ParkSensorFile result = serviceInstance.readDataFile(dataFileName);
            assertNull(result,"If dataFile is empty the method should return null");

            //if dataFile has incorrect information should return null -- missing two lines
            dataFile = new File(Constants.INPUT_FILES_DIRECTORY + dataFileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
            writer.write("ScooterId: 1\n");
            writer.write("ScooterBatteryCapacity: 187\n");
            writer.close();

            result = serviceInstance.readDataFile(dataFileName);
            assertNull(result,"If dataFile has incorrect information should return null");

            //if dataFile has incorrect information should return null -- missing one lines
            writer = new BufferedWriter(new FileWriter(dataFile, true));
            writer.append("ScooterBatteryPercentage: 10\n");
            writer.close();

            result = serviceInstance.readDataFile(dataFileName);
            assertNull(result,"If has incorrect information should return null");

            //if dataFile has correct information return the ParkSensorFile object
            writer = new BufferedWriter(new FileWriter(dataFile,true));
            writer.append("EstimatedTimeToFullCharge(min): 187.50\n");
            writer.append("Reestimate: 1\n");
            writer.close();

            //compare information read to the expected result
            result = serviceInstance.readDataFile(dataFileName);
            assertNotNull(result, "If the file is correctly formatted return should not be null");
            ParkSensorFile expectResult = new ParkSensorFile(1,187,
                                    10,187.50,"2018_01_01_23_12_30");
            assertEquals(expectResult, result);


        } catch (IOException e) {
            fail("exception thrown while testing estimate data file");
        } finally{
            if(dataFile != null){
                dataFile.delete();
            }
        }
    }


    @Test
    void testDeleteFileinInputDirectory() {

        String fileName = "testFileName";
        File dataFile = null;
        try {
            dataFile = new File(Constants.INPUT_FILES_DIRECTORY + fileName);

            assertFalse( serviceInstance.deleteFileInInputDirectory(fileName) );
            dataFile.createNewFile();
            assertTrue( serviceInstance.deleteFileInInputDirectory(fileName) );
            assertFalse( serviceInstance.deleteFileInInputDirectory(fileName) );


        } catch (IOException e) {
            fail("Exception thrown while testing 'deleteFileInInputDirecotry'");
        } finally{
            if(dataFile != null)
                dataFile.delete();
        }
    }

    @ParameterizedTest
    @MethodSource("generateSensorFileContent")
    void validateParkSensorFile(int scooterId,int batteryCapacity,int batteryPercentage, double estimatedTime,
                                String timestamp, boolean expectedResult) {

        ParkSensorFile sensorFile = new ParkSensorFile(scooterId,batteryCapacity ,batteryPercentage,estimatedTime, timestamp);
        assertEquals(expectedResult,serviceInstance.validateParkSensorFile(sensorFile));
    }
    private static Stream<Arguments> generateSensorFileContent() {
        return Stream.of(
                Arguments.of(1,150,75,20.5,"2020_01_25_21_45_15", true),
                Arguments.of(1,150,75,20.5,"2020_01_25 21_45_15", false),//invalid separator
                Arguments.of(1,150,75,20.5,"2020_01_25_21:45:15", false),//invalid separator
                Arguments.of(1,150,75,20.5,"2020_13_25_21_45_15", false),//invalid month
                Arguments.of(1,150,75,20.5,"2020_01_32_21_45_15", false),//invalid day
                Arguments.of(1,150,75,20.5,"2020_01_15_24_45_15", false),//invalid hour
                Arguments.of(1,150,75,20.5,"2020_01_30_21_60_15", false),//invalid min
                Arguments.of(1,150,75,20.5,"2020_01_32_21_12_60", false),//invalid sec
                Arguments.of(-1,150,75,20.5,"2020_01_25_21_45_15", false),//invalid scooterId
                Arguments.of(-11515,150,75,20.5,"2020_01_25_21_45_15", false),//invalid scooterId
                Arguments.of(1,-1,75,20.5,"2020_01_25_21_45_15", false),//invalid batteryCapacity
                Arguments.of(1,150,-75,20.5,"2020_01_25_21_45_15", false), //invalid batteryPercentage
                Arguments.of(1,150,115,20.5,"2020_01_25_21_45_15", false), //invalid batteryPercentage
                Arguments.of(1,150,75,-1,"2020_01_25_21_45_15", false), //invalid estimate
                Arguments.of(1,150,75,-2,"2020_01_25_21_45_15", false), //invalid estimate
                Arguments.of(1187,1500,100,0,"2020_01_25_21_45_15", true),
                Arguments.of(654,100,0,560,"2020_01_25_21_45_15", true)
        );
    }


}