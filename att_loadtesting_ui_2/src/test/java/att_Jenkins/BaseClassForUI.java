package att_Jenkins;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utilityPackage.ReadDataFromPropertyFile;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;

public class BaseClassForUI {
    public static WebDriver driver;
    public static String UAT_url;
    

    // public static void main(String[] args) {
    //     setUpEdgeDriver();

    // }
     public static WebDriver setUpEdgeDriver()
     {

         UAT_url = ReadDataFromPropertyFile.uIData.getProperty("UAT_url");
         ChromeOptions options = new ChromeOptions();
         options.setBinary("/opt/google/chrome/google-chrome");
    //     options.setHeadless(true);
         WebDriverManager.chromedriver().setup();
         driver = new ChromeDriver(options);
         driver.manage().window().maximize();
         driver.manage().deleteAllCookies();
         driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
         driver.get(UAT_url);
         System.out.println("ChromeDirver"+driver);

         return driver;


    // }
    //public static WebDriver setUpEdgeDriver()
   // {

     //   UAT_url = ReadDataFromPropertyFile.uIData.getProperty("UAT_url");
     //  System.setProperty("webdriver.edge.driver", "C:\\Users\\dsbhombe\\Downloads\\msedgedriver.exe");
     
      //  ChromeOptions options = new ChromeOptions();
       // options.setBinary("/opt/google/chrome/google-chrome");
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
//        EdgeOptions edgeOptions = new EdgeOptions();
//        edgeOptions.addArguments("--headless=new");
       // WebDriver driver = new ChromeDriver(options);
        //WebDriver driver = new EdgeDriver();
       // WebDriver driver = new EdgeDriver(    edgeOptions);
        // WebDriverManager.edgedriver().setup();
        // driver = new EdgeDriver();
       // driver.manage().window().maximize();
       // driver.manage().deleteAllCookies();
       // driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
      //  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
       // driver.get(UAT_url);

        //driver.get("http://172.10.0.232:7033/login");


       // System.out.println("EdgeDriver"+driver);
//        return driver;

    }
}
