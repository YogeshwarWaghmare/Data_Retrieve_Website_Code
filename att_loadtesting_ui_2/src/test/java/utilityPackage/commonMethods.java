package utilityPackage;

import att_Jenkins.BaseClassForUI;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class commonMethods extends BaseClassForUI {

    public static Map<String,String> mapForTrackingNumber;
    public static Map<String,String> mapForSerialNumber;
    public static Map<String,String> mapForPSUlNumber;

    // public static WebDriver driver;
    public static By textValueForVI(String input)
    {
        By link=By.xpath("//label[text()='" + input + "']");
        return link;
    }
    public static void dynamicLocatorByTextForVI(String locator)
    {
        driver.findElement(textValueForVI(locator)).click();
    }
    public static By textValueFactoryReset(String input)
    {
        By link=By.xpath("(//label[text()='" + input + "'])[1]");
        return link;
    }
    public static void dynamicLocatorByTextForFactoryReset(String locator)
    {
        driver.findElement(textValueFactoryReset(locator)).click();
    }
    public static By textValueCometicClean(String input)
    {
        By link=By.xpath("(//label[text()='" + input + "'])[2]");
        return link;
    }
    public static void dynamicLocatorByTextForCosmeticClean(String locator)
    {
        driver.findElement(textValueCometicClean(locator)).click();
    }
    public static By textValueCosmeticRepair(String input)
    {
        By link=By.xpath("(//label[text()='" + input + "'])[4]");
        return link;
    }
    public static void dynamicLocatorByTextForCosmeticRepair(String locator)
    {
        driver.findElement(textValueCosmeticRepair(locator)).click();
    }
    public static By textValueKittiing(String input)
    {

        By link=By.xpath("(//label[text()='" + input + "'])[3]");
        return link;
    }
    public static void dynamicLocatorByTextForKitting(String locator)
    {
        driver.findElement(textValueKittiing(locator)).click();
    }

    public static By textValueQC(String input)
    {
        By link=By.xpath("(//label[text()='" + input + "'])[5]");
        return link;
    }
    public static void dynamicLocatorByTextForQC(String locator)
    {
        driver.findElement(textValueQC(locator)).click();
    }


//    public static By (String input)
//    {
//
//        By link=By.xpath("(//label[text()='" + input + "'])[3]");
//        return link;
//    }
//    public static void dynamicLocatorByTextForKitting(String locator)
//    {
//        driver.findElement(textValueKittiing(locator)).click();
//    }

    public static void frameHandler(String frame) {
        driver.switchTo().frame(frame);
    }
    public static void windowHandler(String windows) {
        driver.switchTo().frame(windows);
    }
    public static void selectOptionFromDropdown(WebElement dropdownelement, String value) {
        Select select = new Select(dropdownelement);
        select.selectByVisibleText(value);
    }
    public static void clearText(WebElement element) {

        element.clear();
    }
    public static boolean isElementDisplay(WebElement element)
    {

        return element.isDisplayed();
    }

    public static void clickOnElement(WebElement element)
    {
        // System.out.println("Driver in the common method class"+driver);
        if(element.isDisplayed()){
            element.click();
        }}
    public static void typeText(WebElement element, String value)
    {
        element.sendKeys(value);
    }
    public static void moveToElementUI(WebElement element) {

        Actions act = new Actions(driver);
        act.moveToElement(element).build().perform();
    }
    public static void clickByjavaScript(WebElement element)
    {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", element);
    }
    public static void sendKeysUsingJavaScriptExecutor(WebDriver driver, WebElement element, String text) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].value = arguments[1];", element, text);
    }
    public static String getTextValueFromLocator(WebElement element)
    {
        return element.getText();

    }
    public static String generateRandomString(int length) {
        String characters = "0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public static void randomStringForTrackingNumber(){
        // Example: Generate a random string of length 10
        String randomString = "TR"+generateRandomString(10);
        mapForTrackingNumber =new LinkedHashMap<>();
        mapForTrackingNumber.put("TrackingNumber",randomString);

        System.out.println("Random tracking number in util in class: " + mapForTrackingNumber.get("TrackingNumber"));

    }
    public static void randonStringforSerialNumber(){
        // Example: Generate a random string of length 10
        String randomString = "R"+generateRandomString(14);
        mapForSerialNumber =new LinkedHashMap<>();
        mapForSerialNumber.put("SerialNumber",randomString);
        System.out.println("Random serilanumber in util in class: " + mapForSerialNumber.get("SerialNumber"));

    }
    public static void randonStringforPSUNumber(){
        // Example: Generate a random string of length 10
        String randomString = "PSU"+generateRandomString(5);
        mapForPSUlNumber =new LinkedHashMap<>();
        mapForPSUlNumber.put("PSUNumber",randomString);
        System.out.println("Random PSU number in util in class: " + mapForPSUlNumber.get("PSUNumber"));

    }


}
