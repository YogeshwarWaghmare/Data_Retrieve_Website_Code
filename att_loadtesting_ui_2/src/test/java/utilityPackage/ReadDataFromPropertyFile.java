package utilityPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadDataFromPropertyFile {
    public static Properties test;
    public static Properties data;
    public static Properties uIData;

    static {
        // Load the prop file where we have store tne flows ( i.e. property file for the flows )
//        test = new Properties();
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(System.getProperty(("user.dir")) + "\\src\\test\\Test.properties");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            test.load(fis);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//====================================================================================================
        // Load the prop file where we have store tne TestCases ( i.e. property file for the flows )
        data = new Properties();
        FileInputStream fis1 = null;
        try {
            fis1 = new FileInputStream(System.getProperty(("user.dir")) + "/src/test/java/att_Jenkins/OrgData.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            data.load(fis1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//====================================================================================================
        uIData = new Properties();
        FileInputStream fis2 = null;
        try {
            fis2 = new FileInputStream(System.getProperty(("user.dir")) + "/src/test/java/att_Jenkins/UIProperty.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            uIData.load(fis2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
