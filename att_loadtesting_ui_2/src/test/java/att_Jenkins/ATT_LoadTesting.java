package att_Jenkins;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import utilityPackage.DBUtil;
import utilityPackage.ReadDataFromPropertyFile;

import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static att_Jenkins.BaseClassForUI.setUpEdgeDriver;
import static utilityPackage.DBUtil.getSerialNumberList;
import static utilityPackage.DBUtil.retrievePalletCode;
import static utilityPackage.commonMethods.*;

public class ATT_LoadTesting extends BaseClassForUI {
    public static Map<String, String> mapForShipmentNumber;
    public static Map<String, String> mapForSerialNumber;
    JavascriptExecutor jsExecutor;
    WebDriverWait wait;
    Actions actions;
    public  String nextStep ="";
    public List<String> serialnumbers;
    List<String> masterCartonCode = new ArrayList<>();
    String dataGaylordIdValue;
    String palletCodefromDb;
    String masterCartonNo;
    public int units_per_carton;
    public int cartons_per_pallet;
    public int j;
    String orgId= ReadDataFromPropertyFile.uIData.getProperty("orgId");
    public WebDriver driver;

    @Test(priority=1)
    public void loginToATT() throws SQLException, ClassNotFoundException {

        driver=BaseClassForUI.setUpEdgeDriver();
        DBUtil.getOrgInfo();
        WebElement userName=driver.findElement(By.xpath(ATT_Locators.username));
        typeText(userName,"rlc_automationtest46");

        //userName.sendKeys(DBUtil.userName);

        // convert base64 into
        String PassWord = DBUtil.password;
        //WebElement password = driver.findElement(By.xpath("//input[@id='txt_passwd']"));
        WebElement password = driver.findElement(By.xpath(ATT_Locators.PassWord));
        password.sendKeys("Admin@1234");

        //txt_station
        WebElement stationName = driver.findElement(By.xpath(ATT_Locators.stationName));
        stationName.sendKeys("RLC-STATION-116");
        stationName.sendKeys(Keys.ENTER);


        if(orgId.equals("242")){

            clickOnElement(driver.findElement(By.xpath(ATT_Locators.tmmOrgTile)));

        }else if(orgId.equals("243")){


            clickOnElement(driver.findElement(By.xpath(ATT_Locators.sqOrgTile)));
        }
        else if(orgId.equals("248")){

            clickOnElement(driver.findElement(By.xpath(ATT_Locators.tavOrgTile)));
        }
        else if(orgId.equals("247")){

            clickOnElement(driver.findElement(By.xpath(ATT_Locators.attOrgTile)));
        }
    }
    @Test(priority = 2)
    public void dockreceiving() throws SQLException, ClassNotFoundException, InterruptedException {
        jsExecutor=(JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(300));
        /// Calling getSerialNumbetList Method of DBUtil to get serail number and model from the DATA Base
        //getSerialNumberList();
        Thread.sleep(1000);
        ArrayList<String> newTb = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(newTb.get(1));
//        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.homeButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        //jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.homeButton)));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.dockReceivingOption))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.dockReceivingOption)));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.buildDockReceiving))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildDockReceiving)));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.dropDownForCarrierInformation))));
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.dropDownForCarrierInformation)), "FEDEX");
        randomStringForTrackingNumber();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.trackingNumberField)));
        typeText(driver.findElement(By.xpath(ATT_Locators.trackingNumberField)), mapForTrackingNumber.get("TrackingNumber"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.savButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.savButton)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.successMsgOnDockReceiving)));
        System.out.println("Success msg after dock receiving = " + getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.successMsgOnDockReceiving))));
        String shipmentNbr = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.shipmentReceiveNumber)));
        System.out.println("shipment receive number - " + shipmentNbr);

        mapForShipmentNumber = new LinkedHashMap<>();
        mapForShipmentNumber.put("shipmentNbr", shipmentNbr);
    }

    public void buildGaylord() throws InterruptedException {
        // randonStringforSerialNumber();
        System.out.println("IN BUILD GAYLORD METHOD");
        wait = new WebDriverWait(driver, Duration.ofSeconds(500));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton))));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
        Thread.sleep(1000);
        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock))));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock)));
        //clickOnElement(driver.findElement(By.id(ATT_Locators.buildGaylordBlock)));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(ATT_Locators.buildGaylordOption))));
        clickOnElement(driver.findElement(By.id(ATT_Locators.buildGaylordOption)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.shipmentReceivedTexBox)));
        typeText(driver.findElement(By.xpath(ATT_Locators.shipmentReceivedTexBox)), mapForShipmentNumber.get("shipmentNbr"));
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.selectDeviceTypeFromDropDown)), "GATEWAY");
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.selectItemModelFromDropDown)), DBUtil.modelFromTransactiontbl);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.saveButtonOnGaylordPage))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.saveButtonOnGaylordPage)));

        //Thread.sleep(6000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//img[@id='PrintLabelIcon']"))));

        WebElement imgElement = driver.findElement(By.xpath("//img[@id='PrintLabelIcon']"));
        // Get the value of the data-gaylordid attribute
        dataGaylordIdValue = imgElement.getAttribute("data-gaylordid");
        // Print the retrieved value
        System.out.println("data-gaylordid Value: " + dataGaylordIdValue);

    }

    @Test(priority = 3)
    public void flowByrMAD() throws SQLException, ClassNotFoundException, InterruptedException {
        //serialnumbers=Arrays.asList("RMADNMILX3YYA");
        serialnumbers= getSerialNumberList();
        System.out.println("serialnumbers"+serialnumbers);
        mapForSerialNumber =new LinkedHashMap<>();
//        int devices_per_carton=2,cartons_per_pallete=2;
//        factoryReset();
        buildMaterCarton_Pallet(serialnumbers, masterCartonCode);
    }


    public String executeStep(String stepToExecute,String SN,int j) throws SQLException, InterruptedException, ClassNotFoundException {
        String step;


        System.out.println("stepToExecute-----> "+stepToExecute);

        switch (stepToExecute) {
            case "VISUAL_INSPECTION":
                nextStep = visual_Inspection(SN,j).get("NEXT_STEP_NEW");
                //offsetCountMap.put("visual_Inspection",offsetCountMap.get("visual_Inspection")+1);
                break;
            case "Factory Reset":
                System.out.println("nextstep at factory reset "+nextStep);
                System.out.println("stepToExecute at factory reset "+stepToExecute);
                nextStep = factoryReset(SN,j).get("NEW_NEXT_STEP");
                // offsetCountMap.put("Factory_Reset",offsetCountMap.get("Factory_Reset")+1);
                break;
            case "ATTS":
                nextStep=aTTS_Step(SN).get("NEW_NEXT_STEP");
                // offsetCountMap.put("ATTS_RESET",offsetCountMap.get("ATTS_RESET")+1);
                break;
            case "WIFI":
                nextStep=wIFI_Step().get("NEW_NEXT_STEP");
                // offsetCountMap.put("WIFI_TEST",offsetCountMap.get("WIFI_TEST")+1);

                break;
            case "ETT":
                nextStep=eTT_Step().get("NEW_NEXT_STEP");
                //  offsetCountMap.put("ETT_TEST",offsetCountMap.get("ETT_TEST")+1);

                break;
            case "ATSG":
                nextStep=aTSG_Step().get("NEW_NEXT_STEP");
                // offsetCountMap.put("ATSG_TEST",offsetCountMap.get("ATSG_TEST")+1);

                break;
            case "COSMETIC_CLEAN":
                nextStep=cOSMETIC_CLEAN_Step().get("NEW_NEXT_STEP");
                //offsetCountMap.put("COSMETIC_CLEAN",offsetCountMap.get("COSMETIC_CLEAN")+1);

                break;
            case "COSMETIC_REPAIR":
                nextStep=cOSMETIC_REPAIR_Step().get("NEW_NEXT_STEP");
                // offsetCountMap.put("COSMETIC_REPAIR",offsetCountMap.get("COSMETIC_REPAIR")+1);

                break;
            case "KITTING":
                nextStep=kITTING_Step().get("NEW_NEXT_STEP");
                //offsetCountMap.put("KITTING",offsetCountMap.get("KITTING")+1);
                break;
            case "QUALITY_CHECK":
                nextStep=quality_Check_Step().get("NEW_NEXT_STEP");
                //offsetCountMap.put("QUALITY_CHECK",offsetCountMap.get("QUALITY_CHECK")+1);
                break;
            default:
                System.out.println("This step is not in requirement " + nextStep);
                break;


        }
        return nextStep;
    }
    //        public void flowByrMAD (String step) throws SQLException, ClassNotFoundException, InterruptedException {
//            String modelForGateWay = "";
//            System.out.println("Getting from the rule DB for Visual Inspection");
//            //DBUtil.getDataFromRuleDB();
//
//
//            // List<String> serialnumbers= getSerialNumberList();
//            List<String> serialnumbers = Arrays.asList("RMAD2DHDRTMBK");
//
//            System.out.println("serialnumbers" + serialnumbers);
//
//            mapForSerialNumber = new LinkedHashMap<>();
//            int count = 0;
//
//            for (int i = 0; i < serialnumbers.size(); i++) {
//                if (count == 0) {
//                    buildGaylord();
//                }
//                if (count < 12) {
//                    // if (serialnumbers != null) {
//
//                    System.out.println("Processing serial number: " + serialnumbers.get(i));
//                    mapForSerialNumber.put("sourceSerialNumber", serialnumbers.get(i));
//
//                    typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBox)), serialnumbers.get(i));
//                    System.out.println("Serial number send in the text box");
//                    clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButton)));
//                    Thread.sleep(5000);
//                    System.out.println("click on the search button");
//
//                }
//
//                int viCount = 0;
//                System.out.println("DBUtil.model" + DBUtil.modelFromTransactiontbl);
//                // step =""; // Assign a numeric value to each step
//                if (viCount == 0) {
//                    String nextstep = visual_Inspection();
//                }
//                viCount++;
//
//
//                count++;
//
//            }
//            if (count == 12) {
//                count = 0;
//
//                // buildMaterCarton();
//            }
//        }
    public void buildMaterCarton (List < String > SN) {
        //clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonTab)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild)));
        String masterCartonNo = driver.findElement(By.xpath(ATT_Locators.masterCartonNoSearch)).getText();
        System.out.println("******MASTER CARTON NO : " + masterCartonNo);
        for (int i = 0; i < 2; i++) {
            driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).sendKeys(SN.get(i));
            driver.findElement(By.xpath(ATT_Locators.masterCartonSNScanSearch)).click();
        }
        driver.findElement(By.xpath(ATT_Locators.masterCartonClose)).click();
    }

    public void flowByBlindReceipt() throws InterruptedException {
        typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBox)), mapForSerialNumber.get("SerialNumber"));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButton)));
        Thread.sleep(3000);
        //System.out.println("Warning pop for blind receipt with msg - "+getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.warningPopUp))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.answerAsYes)));
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.itemNumber)), "100005278-R");
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.nextButton)));

        System.out.println("successMsgAfterBlindReceipt -" + getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.successMsgAfterBlindReceipt))));
        Thread.sleep(3000);
    }
    public void buildMaterCarton_Pallet(List<String> SN, List<String> masterCartonCode) throws InterruptedException, SQLException, ClassNotFoundException {
        jsExecutor = (JavascriptExecutor) driver;
        DBUtil.getCartonPalletCount();
        buildGaylord();

        units_per_carton = Integer.parseInt(DBUtil.unitesPerCarton);
        cartons_per_pallet = Integer.parseInt(DBUtil.unitesPerPallet);
        System.out.println("units_per_carton in buildMaterCarton_Pallet" + units_per_carton);
        System.out.println("cartons_per_pallet in buildMaterCarton_Pallet" + cartons_per_pallet);

//        offsetCountMap.put("visual_Inspection",0);
//        offsetCountMap.put("Factory_Reset", 0);
//        offsetCountMap.put("ATTS_RESET", 0);
//        offsetCountMap.put("WIFI_TEST", 0);
//        offsetCountMap.put("ETT_TEST", 0);
//        offsetCountMap.put("ATSG_TEST", 0);
//        offsetCountMap.put("COSMETIC_CLEAN", 0);
//        offsetCountMap.put("COSMETIC_REPAIR", 0);
//        offsetCountMap.put("KITTING", 0);
//        offsetCountMap.put("QUALITY_CHECK", 0);


        for(int i = 0;i<cartons_per_pallet; i++)
        {
            for(j = 0;j<units_per_carton; j++)
            {

                int viCount = 0;
                nextStep="";
                while (!nextStep.equals("BUILD_CARTON")) {
                    if (viCount == 0) {
                        nextStep = executeStep("VISUAL_INSPECTION",serialnumbers.get(j), j);
                        System.out.println("serialnumbers.get(j) in while loop"+serialnumbers.get(j));
                    } else {
                        nextStep = executeStep(nextStep,serialnumbers.get(j),j);
                        System.out.println("nextStep in while loop "+ nextStep);


                    }
                    viCount++;
                }
                if(units_per_carton == 1){
                    System.out.println("units_per_carton is one");
                }
                else {
//                    Thread.sleep(5000);
//                    //clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
//                    jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.homeButton)));
//                    Thread.sleep(2000);
//                    clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordOption)));
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton))));
                    jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
                    Thread.sleep(1000);
                    // clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));

                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock))));
                    jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock)));
                    //clickOnElement(driver.findElement(By.id(ATT_Locators.buildGaylordBlock)));
//                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(ATT_Locators.buildGaylordOption))));
//                    clickOnElement(driver.findElement(By.id(ATT_Locators.buildGaylordOption)));
                    driver.findElement(By.xpath(ATT_Locators.gaylordTextBox)).sendKeys(dataGaylordIdValue);
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.gaylordSearchbtn))));
                    clickOnElement(driver.findElement(By.xpath(ATT_Locators.gaylordSearchbtn)));
                }
            }
            System.out.println("Outside QC");
            Thread.sleep(4000);
          //  clickOnElement(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonTab)));
            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild))));
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild)));

//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
//            wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.masterCartonSNScan))));



            for(int k=0;k<units_per_carton;k++) {
               // Thread.sleep(2000);
                wait = new WebDriverWait(driver, Duration.ofSeconds(200));
               // wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.masterCartonSNScan))));
               // wait.until(ExpectedConditions.elementToBeClickable((By.xpath(ATT_Locators.masterCartonSNScan))));
                Thread.sleep(30000);
                driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).sendKeys(serialnumbers.get(k));
               // jsExecutor.executeScript("arguments[0].value=(serialnumbers.get(k)",web)
                System.out.println("Serial number value with variable k "+serialnumbers.get(k));
                System.out.println(serialnumbers + "serialnumbers in master carton loop");
                // Thread.sleep(2000);
                // Thread.sleep(2000);
                // driver.findElement(By.xpath(ATT_Locators.masterCartonSNScanSearch)).click();
                wait = new WebDriverWait(driver, Duration.ofSeconds(200));
                //wait.until(ExpectedConditions.elementToBeClickable((By.xpath(ATT_Locators.masterCartonSNScanSearch))));
                Thread.sleep(30000);
                jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.masterCartonSNScanSearch)));
                Thread.sleep(30000);
                //WebElement issueText = driver.findElement(By.xpath(ATT_Locators.masterCartonIssueText));
//                if(driver.findElement(By.xpath(ATT_Locators.masterCartonIssueText)).isDisplayed()) {
//                    System.out.println("ERROR : Master carton contain more than one device");
//                    Thread.sleep(2000);
//                    jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.masterCartonSNScanSearch)));
//                }
//                else
//                    System.out.println("NO ISSUE ON MASTER CARTON");

//                wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//                wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//th[text()='SERIAL NUMBER']"))));

                //wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[text()='Device added in master carton successfully']"))));
//

                //Thread.sleep(30000);

                if(driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).isEnabled()){
                    driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).clear();
                }
                //Thread.sleep(2000);
//                wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//button[@id='mc_code_btn']"))));
//                driver.findElement(By.xpath("//button[@id='mc_code_btn']")).click();

                masterCartonNo=DBUtil.getMasterCartonId(serialnumbers.get(k));
            }
            Thread.sleep(2000);

            //masterCartonNo=driver.findElement(By.xpath(ATT_Locators.masterCartonID)).getText();
            System.out.println("Master carton Number = " + masterCartonNo);
            masterCartonCode.add(masterCartonNo);
            Thread.sleep(2000);
            Thread.sleep(2000);
            System.out.println("******MASTER CARTON CODE : "+masterCartonNo);
            Thread.sleep(3000);
            Thread.sleep(3000);
            // driver.findElement(By.xpath(ATT_Locators.masterCartonClose)).click();
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.masterCartonClose)));

            Thread.sleep(4000);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
//            Thread.sleep(2000);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordOption)));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton))));
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock))));
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.buildGaylordBlock)));
            driver.findElement(By.xpath(ATT_Locators.gaylordTextBox)).sendKeys(dataGaylordIdValue);
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.gaylordSearchbtn)));

            Thread.sleep(2000);
            Thread.sleep(2000);
            serialnumbers= getSerialNumberList();
            System.out.println("serialnumbers in ATTReceiving" + serialnumbers);
            Thread.sleep(2000);
        }
//        serialnumbers=Arrays.asList("RMADJ8ZDMH5FB","RMADZOEENS1NU");
//        getSerialNumberList();

        clickOnElement(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.palletTab)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.buildPalletButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildPalletButton)));
        Thread.sleep(1000);
        String palletCode=driver.findElement(By.xpath(ATT_Locators.palletCode)).getText();
        Thread.sleep(1000);
        System.out.println("******Pallet NO : "+palletCode);

        for(int l=0;l<cartons_per_pallet;l++) {
//            (serialnumbers.get(k));
            System.out.println("cartons_per_pallet=" + cartons_per_pallet);
//            Thread.sleep(5000);
            Thread.sleep(30000);
//            wait = new WebDriverWait(driver, Duration.ofSeconds(300));
//            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.masterCartonNoSearch)));
            driver.findElement(By.xpath(ATT_Locators.masterCartonNoSearch)).sendKeys(masterCartonCode.get(l));
            String masterCarton_string = masterCartonCode.get(l);
            //jsExecutor.executeScript("document.getElementsById('mc_code').value="+masterCarton_string+";");
            //jsExecutor.executeScript("arguments[0].value="+masterCarton_string+","+ ATT_Locators.masterCartonNoSearch);
            System.out.println("masterCartonCode"+masterCartonCode);
            Thread.sleep(30000);
//            wait = new WebDriverWait(driver, Duration.ofSeconds(300));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.palletMasterCartonScanSearch))));
            driver.findElement(By.xpath(ATT_Locators.palletMasterCartonScanSearch)).click();
            //jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.palletMasterCartonScanSearch)));
            Thread.sleep(30000);
            if(driver.findElement(By.xpath(ATT_Locators.masterCartonNoSearch)).isEnabled()){
                driver.findElement(By.xpath(ATT_Locators.masterCartonNoSearch)).clear();
            }
            Thread.sleep(5000);
            if(cartons_per_pallet == l+1){
                palletCodefromDb= retrievePalletCode(masterCartonCode.get(l));
                System.out.println("palletCodefromDb ="+ palletCodefromDb);
            }

        }
        Thread.sleep(2000);
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.palletClose)));

        //driver.findElement(By.xpath(ATT_Locators.palletClose)).click();

        Thread.sleep(1000);
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.areUSurePalletClosePopup)));

        // driver.findElement(By.xpath(ATT_Locators.areUSurePalletClosePopup)).click();
        Thread.sleep(1000);
        // call inva

        DBUtil.validateINVA(serialnumbers.get(j));
        if(DBUtil.from_location!=null && DBUtil.to_location !=null && DBUtil.from_location.equalsIgnoreCase("WIP") && DBUtil.to_location.equalsIgnoreCase("RVAR"))
        {
            System.out.println("From location on pallet screen "+DBUtil.from_location+ ", To location on pallet screen "+DBUtil.to_location);
            System.out.println("From location and To location is as Expected on pallet screen");
        }
        else
        {
            System.out.println("From location and To location is not as Expected on pallet screen");
        }
        build_ASN();
    }

    public Map<String, String> visual_Inspection (String SN,int j) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(300));
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.serialNumberTextBox)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ATT_Locators.closegaylord)));
        
        typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBox)),  serialnumbers.get(j));
        Thread.sleep(3000);
        System.out.println("Serial number send in the text box");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.searchButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButton)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.visualInspectionQuestionText)));


        System.out.println("click on the search button");

        Map<String, String> dataForVisualInspection = DBUtil.getDataFromRuleDB("tblvisualinspection", "Visual_Inspection",DBUtil.modelFromTransactiontbl); //offsetCountMap);
        String result = dataForVisualInspection.get("Visual_Inspection");



//        if (result.equals("Pass")) {
//            dynamicLocatorByTextForVI(result);
//        } else {
//            dynamicLocatorByTextForVI(result);
//            Thread.sleep(2000);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.error_CodeOnVI)));
//            typeText(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBox)), "4300");
//            //driver.findElement(By.xpath(ATT_Locators.error_Code)).sendKeys(Keys.ENTER);
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.submitVI))));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitVI)));
        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.successMsgOnVisualInspection))));
        System.out.println("success Msg On VisualInspection - " + getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.successMsgOnVisualInspection))));

        // Validation of RMAR on Visual Inspection
        Thread.sleep(4000);
        DBUtil.validateRMAR(serialnumbers.get(j));
        //nextSteps.add(DBUtil.nextStepOnVI);
        //step=DBUtil.nextStepOnVI;
        // return dataForVisualInspection.get("NEXT_STEP_NEW");
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.homeButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
         wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.assetTraceability))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.assetTraceability)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.searchBoxOnAssetTraceability)));
        typeText(driver.findElement(By.xpath(ATT_Locators.searchBoxOnAssetTraceability)),SN);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.clickOnSearchButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickOnSearchButton)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.nextStepOnAssetTraceability)));

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.nextStepOnAssetTraceability)));
        System.out.println("actualNextStepValue from asset Traceability"+actualNextStepValue);

        if (actualNextStepValue.equalsIgnoreCase(dataForVisualInspection.get("NEXT_STEP_NEW"))) {
            System.out.println("Visual Inspection Result : Pass");
            return dataForVisualInspection;

        } else {
            System.out.println("Visual Inspection Result : Fail");
            return null;
        }


    }

    public Map<String, String> factoryReset (String SN,int j) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForFactoryReset = DBUtil.getDataFromRuleDB("tblfactoryreset", "Factory_Reset",DBUtil.modelFromTransactiontbl); // offsetCountMap);

        System.out.println("serial number in wip screen " + serialnumbers.get(j));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.wipScreenOption)));
        typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBoxOnWIP)), serialnumbers.get(j));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButtonOnWipScreen)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.factoryResetQuestionText)));


        String result = dataForFactoryReset.get("Factory_Reset");
//        if (result.equals("Pass")) {
//            Thread.sleep(2000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            wait = new WebDriverWait(driver, Duration.ofSeconds(90));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnFactoryReset))));
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(90));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnFactoryReset))));
//            Thread.sleep(3000);
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnFactoryReset)), "2110");
//            //actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(4000);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickOnErrorCode)));
//
//        }
        WebElement saveButtonOnFactoryReset = wait.until(ExpectedConditions.elementToBeClickable(By.id("save")));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.id("save")));
        Thread.sleep(7000);
        // Validate INVA
        DBUtil.validateINVA(serialnumbers.get(j));

        if (DBUtil.from_location!=null && DBUtil.to_location !=null && DBUtil.from_location.equalsIgnoreCase("RETURN") && DBUtil.to_location.equalsIgnoreCase("WIP")) {
            System.out.println("From location on Factory Reset " + DBUtil.from_location + ", To location on Factory Rest " + DBUtil.to_location);
            System.out.println("From location and To location is as Expected on Factory Reset");
        }
        else {
            System.out.println("From location and To location is not as Expected on Factory rest");
        }


        System.out.println("Factory Reset completed Successfully");


        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("actualNextStepValue on Wip screen--->"+actualNextStepValue);
        System.out.println("Expected Next Step---->"+dataForFactoryReset.get("NEW_NEXT_STEP"));

        if (actualNextStepValue.contains(dataForFactoryReset.get("NEW_NEXT_STEP"))) {
            System.out.println("factoryReset Result : Pass");
            return dataForFactoryReset;
        } else {
            System.out.println("factoryReset Result : Fail");
            return null;
        }
    }

    public Map<String, String> aTTS_Step (String SN) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForAttsStep = DBUtil.getDataFromRuleDB("tblatts", "ATTS_RESET",DBUtil.modelFromTransactiontbl); // offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNATTs)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        Thread.sleep(3000);

        String result = dataForAttsStep.get("ATTS_RESET");

//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnFactoryReset)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.aTTQuestionText)));

        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonOnFactoryReset)));
        System.out.println("ATTS step completed Successfully");

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on ATTS Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on ATTS Step---->"+dataForAttsStep.get("NEW_NEXT_STEP"));

        if (actualNextStepValue.contains(dataForAttsStep.get("NEW_NEXT_STEP"))) {
            System.out.println("ATTS Result : Pass");
            return dataForAttsStep;
        } else {
            System.out.println("ATTS Result : Fail");
            return null;
        }
    }
    public Map<String, String> wIFI_Step () throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForWifiStep = DBUtil.getDataFromRuleDB("tblwifi", "WIFI_TEST",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNWIFI)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        Thread.sleep(3000);

        String result = dataForWifiStep.get("WIFI_TEST");
//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)), "3100");
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//
//
//            actions.sendKeys(Keys.ENTER).perform();
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.wIFIQuestionText)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonOnFactoryReset)));
        System.out.println(" WIFI step completed Successfully");

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on Wifi Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on Wifi step---->"+dataForWifiStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForWifiStep.get("NEW_NEXT_STEP"))) {
            System.out.println("WIFI Result : Pass");
            return dataForWifiStep;
        } else {
            System.out.println("WIFI Result : Fail");
            return null;
        }

    }
    public Map<String, String> eTT_Step () throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForEttStep = DBUtil.getDataFromRuleDB("tblett", "ETT_TEST",DBUtil.modelFromTransactiontbl);// offsetCountMap);
        // click on the clickable SN

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNETT)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        String result = dataForEttStep.get("ETT_TEST");
//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)), "2610");
//            Thread.sleep(3000);
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(3000);
//            actions.sendKeys(Keys.ENTER).perform();
//
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.eTTQuestionText)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonOnFactoryReset)));
        System.out.println(" ETT step completed Successfully");

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on ETT Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on ETT step---->"+dataForEttStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForEttStep.get("NEW_NEXT_STEP"))) {
            System.out.println("ETT Result : Pass");
            return dataForEttStep;
        } else {
            System.out.println("ETT Result : Fail");
            return null;
        }

    }
    public Map<String, String> aTSG_Step () throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForAtsgStep = DBUtil.getDataFromRuleDB("tblatsg", "ATSG_TEST",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNATSG)));
        // click on the clickable SN
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.aTSGQuestionText)));
        String result = dataForAtsgStep.get("ATSG_TEST");
//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)), "2610");
//            Thread.sleep(3000);
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(3000);
//            actions.sendKeys(Keys.ENTER).perform();
//
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.aTSGQuestionText)));
        // Pass the ATTS Stpe
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonOnFactoryReset)));
        System.out.println(" ETT step completed Successfully");

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on ATSG Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on ATSG step---->"+dataForAtsgStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForAtsgStep.get("NEW_NEXT_STEP"))) {
            System.out.println("ATSG Result : Pass");
            return dataForAtsgStep;
        } else {
            System.out.println("ATSG Result : Fail");
            return null;
        }

    }
    public Map<String, String> cOSMETIC_CLEAN_Step () throws InterruptedException, SQLException, ClassNotFoundException {
        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForCosmeticCleanStep = DBUtil.getDataFromRuleDB("tblcosmeticclean", "COSMETIC_CLEAN",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNComseticClean)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.cosmeticCleanQuestionText)));
        String result = dataForCosmeticCleanStep.get("COSMETIC_CLEAN");
//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForCosmeticClean(result);
//        } else {
//            dynamicLocatorByTextForCosmeticClean(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//            actions.sendKeys(Keys.ENTER).perform();
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.cosmeticCleanQuestionText)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonOnCosmeticClean)));
        System.out.println(" cosmetic clean step completed Successfully");

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on COSMETIC_CLEAN Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on COSMETIC_CLEAN step---->"+dataForCosmeticCleanStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForCosmeticCleanStep.get("NEW_NEXT_STEP"))) {
            System.out.println("COSMETIC_CLEAN Result : Pass");
            return dataForCosmeticCleanStep;
        } else {
            System.out.println("COSMETIC_CLEAN Result : Fail");
            return null;
        }

    }
    public Map<String, String> cOSMETIC_REPAIR_Step () throws SQLException, ClassNotFoundException, InterruptedException {
        Map<String, String> dataForCosmeticRepairStep = DBUtil.getDataFromRuleDB("tblcosmeticrepair", "COSMETIC_REPAIR",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        // click on the clickable SN

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNComseticRepair)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        String result = dataForCosmeticRepairStep.get("COSMETIC_REPAIR");
//        if (result.equals("Pass")) {
//
//            dynamicLocatorByTextForCosmeticRepair(result);
//        } else {
//            dynamicLocatorByTextForCosmeticRepair(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnCosmeticClean)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//            actions.sendKeys(Keys.ENTER).perform();
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.cosmeticRepairQuestionText)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.submitButtonOnCosmeticRepair)));

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on COSMETIC_REPAIR Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on COSMETIC_REPAIR step---->"+dataForCosmeticRepairStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForCosmeticRepairStep.get("NEW_NEXT_STEP"))) {
            System.out.println("COSMETIC_REPAIR Result : Pass");
            return dataForCosmeticRepairStep;
        } else {
            System.out.println("COSMETIC_REPAIR Result : Fail");
            return null;
        }

    }

    public Map<String, String> kITTING_Step () throws InterruptedException, SQLException, ClassNotFoundException {
        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForKittingStep = DBUtil.getDataFromRuleDB("tblkitting", "KITTING",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        randonStringforPSUNumber();
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNKitting)));
        // click on the clickable SN
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.textBoxForPSUNumber)));
        // type PSU number
        typeText(driver.findElement(By.xpath(ATT_Locators.textBoxForPSUNumber)), mapForPSUlNumber.get("PSUNumber"));
        // click on search button of PSU
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButtonForPSU)));
        // answer as Yes for PSU pop up
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.asnwerAsYesForPSUPopUp)));
        Thread.sleep(3000);
        if (driver.findElement(By.xpath(ATT_Locators.warningPop)).isDisplayed()) {
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.closeWarningPopUp)));
        }

        String result = dataForKittingStep.get("KITTING");
        wait = new WebDriverWait(driver, Duration.ofSeconds(150));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.kittingQuestionText)));
//        if (result.equals("Pass")) {
//            Thread.sleep(3000);
//            dynamicLocatorByTextForKitting(result);
//        } else {
//            dynamicLocatorByTextForKitting(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//            actions.sendKeys(Keys.ENTER).perform();
//        }
        // submit the kitting step
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.kittingQuestionText)));

        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonForKitting)));
        System.out.println(" kITTING step completed Successfully");
        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.submitButtonForKitting)));

        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on KITTING Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on KITTING step---->"+dataForKittingStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForKittingStep.get("NEW_NEXT_STEP"))) {
            System.out.println("KITTING Result : Pass");
            return dataForKittingStep;
        } else {
            System.out.println("KITTING Result : Pass");
            return null;
        }

    }

//    public void quality_Check_Step(List<String> SN)
//    {
////        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
////        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNQC)));
////        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
//        for(int i=0;i<2;i++) {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            driver.findElement(By.xpath(ATT_Locators.qcSearchSN)).sendKeys(SN.get(i));
//            driver.findElement(By.xpath(ATT_Locators.searchButton)).click();
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.qCQuestionText)));
//            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonQualityCheck)));
//            System.out.println(" quality_Check step completed Successfully");
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.masterCartonText)));
////            clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
////            clickOnElement(driver.findElement(By.xpath(ATT_Locators.wipScreenOption)));
//        }
//        System.out.println("Outside QC");
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
//        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.submitButtonQualityCheck)));
//    }


    public Map<String, String> quality_Check_Step () throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForQualityCheckStep = DBUtil.getDataFromRuleDB("tblqualitycheck", "QUALITY_CHECK",DBUtil.modelFromTransactiontbl);// offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNQC)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.clickableSN)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.qCQuestionText)));
        String result = dataForQualityCheckStep.get("QUALITY_CHECK");
//        if (result.equals("Pass")) {
//            dynamicLocatorByTextForQC(result);
//        } else {
//            dynamicLocatorByTextForQC(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnQC)));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnQC))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnQC)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//            actions.sendKeys(Keys.ENTER).perform();
//        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.qCQuestionText)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.submitButtonQualityCheck)));
        System.out.println(" quality_Check step completed Successfully");
        String actualNextStepValue = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.actualNextStepOnWip)));
        System.out.println("ActualNextStepValue on Wifi Step--->"+actualNextStepValue);
        System.out.println("Expected Next Step on Wifi step---->"+dataForQualityCheckStep.get("NEW_NEXT_STEP"));
        if (actualNextStepValue.contains(dataForQualityCheckStep.get("NEW_NEXT_STEP"))) {
            System.out.println("QUALITY_CHECK Result : Pass");
            return dataForQualityCheckStep;
        } else {
            System.out.println("QUALITY_CHECK Result : Fail");
            return null;
        }        }

    public void build_ASN() throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        //Thread.sleep(1000);
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.receivingHomeButton)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
       // Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.asnTab)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnTab)));
        //Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.buildAsnButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildAsnButton)));
        //Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.asnSelectLocation)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnSelectLocation)));
       // Thread.sleep(1000);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.asnSelectLocation)));
        typeText(driver.findElement(By.xpath(ATT_Locators.asnSelectLocation)),"FDA:TX");
        Thread.sleep(1000);
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.asnSaveLocation)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnSaveLocation)));
//        for(int i= 0; i<pallet_Id_Created.size(); i++){
        Thread.sleep(2000);
        Thread.sleep(2000);
        System.out.println("Pallet code in ASN - " + palletCodefromDb);
//        jsExecutor.executeScript("arguments[0].value = arguments[1];", driver.findElement(By.xpath(ATT_Locators.palletCodeOnAsn)),palletCodefromDb);
//        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
//
//        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.palletCodeOnAsn)));
        Thread.sleep(60000);
        typeText(driver.findElement(By.xpath(ATT_Locators.palletCodeOnAsn)),palletCodefromDb);

        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.palletCodeBtnOnAsn)));
        Thread.sleep(60000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.palletCodeBtnOnAsn)));

//        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.addShippingDateTimePopupBtn)));

        Thread.sleep(60000);
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.addShippingDateTimePopupBtn)));
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.addShippingDateTimePopupBtn)));


//        WebElement dateBox = driver.findElement(By.xpath(ATT_Locators.calenderselectDate));
//        dateBox.sendKeys("03202024");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.calenderselectDate)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.calenderselectDate)));

        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.calenderselectDate)));
        typeText(driver.findElement(By.xpath(ATT_Locators.shippingDatePopupOnAsn)),"11/21/2024");
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.shippingTimePopupOnAsn)));
        //  clickOnElement(driver.findElement(By.xpath(ATT_Locators.calenderselectTime)));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ATT_Locators.calenderselectTime)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.calenderselectTime)));

        clickOnElement(driver.findElement(By.xpath("//span[text()='1']")));  // click on 1 pm
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath("//span[@class='mdtp__button ok']"))); // click on Ok button
//        typeText(driver.findElement(By.xpath(ATT_Locators.shippingTimePopupOnAsn)),"10:06:22PM");

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.saveButtonPopupOnAsn))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.saveButtonPopupOnAsn)));

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.asnClose))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnClose)));


        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.areUSureASNClosePopup))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.areUSureASNClosePopup)));
        Thread.sleep(2000);
        Thread.sleep(2000);
        // call inva
        DBUtil.validateINVA(serialnumbers.get(j));

        if(DBUtil.from_location!=null && DBUtil.to_location !=null && DBUtil.from_location.equalsIgnoreCase("RVAR") && DBUtil.to_location.equalsIgnoreCase("Bulk"))
        {
            System.out.println("From location on ASN screen "+DBUtil.from_location+ ", To location on  ASN screen "+DBUtil.to_location);
            System.out.println("From location and To location is as Expected on  ASN screen");
        }
        else
        {
            System.out.println("From location and To location is not as Expected on  ASN screen");
        }

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.shipASNButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.shipASNButton)));

        wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.addTrackingNoPopup))));
        typeText(driver.findElement(By.xpath(ATT_Locators.addTrackingNoPopup)),"3456789012345");

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.addTrackingNoSaveBtn))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.addTrackingNoSaveBtn)));

    }
}
