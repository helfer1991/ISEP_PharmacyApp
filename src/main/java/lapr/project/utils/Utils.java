package lapr.project.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


/**
 *
 * @author 1090052 1100241
 */
public class Utils {

    private Utils(){
    }

    /**
     * Conversão de String para Inteiro
     *
     * @param s String a ser convertida
     * @return inteiro
     */
    public static int convertStringToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Conversão de String para Short
     *
     * @param s String a ser convertida
     * @return short
     */
    public static short convertStringToShort(String s) {
        try {
            return Short.parseShort(s);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Conversão de String para Float
     *
     * @param s String a ser convertida
     * @return Float
     */
    public static float convertStringToFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * Conversão de String para Float
     *
     * @param s String a ser convertida
     * @return Float
     */
    public static double convertStringToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public static double distBetweenPointsOnEarth(double latA, double lonA, double latB, double lonB) {
        double lat1 = latA;
        double lon1 = lonA;
        double lat2 = latB;
        double lon2 = lonB;
        // shortest distance over the earth's surface
        // https://www.movable-type.co.uk/scripts/latlong.html
        final double R = 6371e3;
        double theta1 = Math.toRadians(lat1);
        double theta2 = Math.toRadians(lat2);
        double deltaTheta = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);
        double a = Math.sin(deltaTheta / 2) * Math.sin(deltaTheta / 2)
                + Math.cos(theta1) * Math.cos(theta2)
                * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = (R * c); // distância em meters
        return d;
    }
    
    /**
     * Leitura do ficheiro de dados e guarda os dados em memória
     *
     * @param filePath - camminho do ficheiro a ser lido
     * @return lista de strings dos dados registados no ficheiro lido
     * @throws Exception
     */
    public static List<String> readFromFile(String filePath) throws Exception {

        BufferedReader bufferedReader = null;
        try {
            List<String> lines = new ArrayList<>();
            bufferedReader = new BufferedReader(new FileReader(filePath));
            bufferedReader.readLine();
            String line;
            while (((line = bufferedReader.readLine()) != null)) {
                lines.add(line);
            }

            return lines;
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File not found");
        }finally{
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }
    }
    
    
}
