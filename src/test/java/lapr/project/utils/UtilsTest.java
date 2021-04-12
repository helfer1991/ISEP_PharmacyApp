package lapr.project.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author 1090052 1100241
 */
public class UtilsTest {


    /**
     * Test of convertStringToInt method, of class Utils.
     */
    @Test
    public void testConvertStringToInt() {
        System.out.println("convertStringToInt");
        String s = "";
        int expResult = 0;
        int result = Utils.convertStringToInt(s);
        Assertions.assertEquals(expResult, result);

        expResult = 2;
        result = Utils.convertStringToInt("2");
        Assertions.assertEquals(expResult, result);

    }
    
    
    /**
     * Test of convertStringToInt method, of class Utils.
     */
    @Test
    public void testConvertStringToShort() {
        System.out.println("convertStringToShort");
        String s = "";
        int expResult = 0;
        int result = Utils.convertStringToInt(s);
        Assertions.assertEquals(expResult, result);

        expResult = 2;
        result = Utils.convertStringToShort("2");
        Assertions.assertEquals(expResult, result);

        expResult = 0;
        result = Utils.convertStringToShort(null);
        Assertions.assertEquals(expResult, result);

    }

    /**
     * Test of convertStringToFloat method, of class Utils.
     */
    @Test
    public void testConvertStringToFloat() {
        System.out.println("convertStringToFloat");
        String s = "";
        float expResult = 0.0F;
        float result = Utils.convertStringToFloat(s);
        Assertions.assertEquals(expResult, result, 0.0);
        
        s = "1.1";
        expResult = 1.1F;
        result = Utils.convertStringToFloat(s);
        Assertions.assertEquals(expResult, result, 0.0);

    }
    
    /**
     * Test of convertStringToFloat method, of class Utils.
     */
    @Test
    public void testConvertStringToDouble() {
        System.out.println("convertStringToDouble");
        String s = "";
        double expResult = 0.0F;
        double result = Utils.convertStringToDouble(s);
        Assertions.assertEquals(expResult, result, 0.0);
        
        s = "1.1";
        expResult = 1.1F;
        result = Utils.convertStringToFloat(s);
        Assertions.assertEquals(expResult, result, 0.0);

        s = null;
        expResult = 0.0F;
        result = Utils.convertStringToFloat(s);
        Assertions.assertEquals(expResult, result, 0.0);
    }
    
    /**
     * Test of distBetweenPointsOnEarth method, of class Utils.
     */
    @Test
    public void testDistEntreDoisLocais() {
        double latA = 41.1;
        double lonA = -8.5;
        double latB = 41;
        double lonB = -8.4;
        double expResult = 13927D;
        double result = Utils.distBetweenPointsOnEarth(latA, lonA, latB, lonB);

        Assertions.assertEquals(expResult, result, 1.0);
    }


}
