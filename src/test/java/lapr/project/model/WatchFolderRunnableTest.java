package lapr.project.model;

import lapr.project.controller.NotifyCourierController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WatchFolderRunnableTest {

    @Mock
    private NotifyCourierController controllerInstance;

    @InjectMocks
    private WatchFolderRunnable instance;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        instance = new WatchFolderRunnable(controllerInstance);
    }


    @Test
    void run_nothingDetected() {

        //class has infinite loop so new thread is used to start and stop execution
        Thread tempThread = new Thread(instance);
        tempThread.start();

        //no file was created so the controller method should be called
        verifyNoInteractions(controllerInstance);
        tempThread.interrupt();

    }
/*
    @Test
    void run_fileDetected() {

        String fileName = "testFileName.txt";
        Thread tempThread = new Thread(instance);
        File testFile = null;
        try {
            //class has infinite loop so new thread is used to start and stop execution
            tempThread.start();
            sleep(2000); //The thread running WatchFolderRunnable  needs some time to 'start' the loop
            when(controllerInstance.processDetectedFile(fileName)).thenReturn(true);

            testFile = new File(Constants.INPUT_FILES_DIRECTORY + fileName);
            testFile.createNewFile();

            //a file was created so the controller method should be called
            verify(controllerInstance).processDetectedFile(fileName);
            tempThread.interrupt();

        } catch (Exception e) {
            fail("exception thrown while testing 'WatchFolderRunnable'");
        } finally {
            if (testFile != null) {
                testFile.delete();
            }
        }

    }

 */

}