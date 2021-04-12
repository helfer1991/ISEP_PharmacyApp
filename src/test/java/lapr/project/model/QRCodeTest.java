package lapr.project.model;

import lapr.project.ui.QRCode;
import com.google.zxing.WriterException;
import lapr.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QRCodeTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void generateQRCodeImage() {
        File file = null;
        try{
            QRCode.generateQRCodeImage("Scooter Id 24645",
                    350,
                    350,
                    Constants.INPUT_FILES_DIRECTORY+"testQrCode.png");

            file = new File(Constants.INPUT_FILES_DIRECTORY+"testQrCode.png");
        }catch (Exception e){
            fail(e.getMessage());
        }finally{
            if(file != null){
                file.delete();
            }
        }
    }

    @Test
    void getQRCodeImage() throws IOException, WriterException {
        byte[] result = QRCode.getQRCodeImage("ScooterId 2555", 350, 350);
        assertTrue(result.length>0);
        assertNotNull(result);
    }
}