package lapr.project.ui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Class with static methods relative to the QrCode
 * Credits to https://www.callicoder.com/generate-qr-code-in-java-using-zxing/
 */
public class QRCode {

    private QRCode(){

    }


    /**
     * Generates a QR code according to the parameters and saves it in the fileSystem
     * @param text content to be stored in the QrCode
     * @param width width of the qrCode image (pixels)
     * @param height height of the qrCode image (pixels)
     * @param filePath filepath(directory + fileName) where the QRCode will be saved
     * @throws WriterException exception while writing QRCode
     * @throws IOException  exception for filesystem errors
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);  // <---------- IMAGE FORMAT
    }

    /**
     * Generates a Qr code according to the parameters and returns the QR core in a byte array format
     * @param text content to be stored in the QrCode
     * @param width width of the qrCode image (pixels)
     * @param height height of the qrCode image (pixels)
     * @return returns the QRCode as a byteArray to be saved
     * @throws WriterException exception while writing QRCode
     * @throws IOException exception for filesystem errors
     */
    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

}
