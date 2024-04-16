package att_Jenkins;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
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
//import java.util.concurrent.TimeUnit;

import static utilityPackage.DBUtil.*;
import static utilityPackage.commonMethods.*;

public class ATTE2E_UIFlows extends BaseClassForUI {
/*
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
    public int viOffsetCount=0;
    String orgId= ReadDataFromPropertyFile.uIData.getProperty("orgId");
    public WebDriver driver;

    Map<String, String> inputTestDataATT;
    List<String> proceesedSerialNumberList;

    // FluentWait fluentwait;



    @Test(priority=1)
    public void loginToATT() throws SQLException, ClassNotFoundException {

        driver=setUpDriver();
        DBUtil.getOrgInfo();
        WebElement userName=driver.findElement(By.xpath(ATT_Locators.username));
        typeText(userName,DBUtil.userName);

        //userName.sendKeys(DBUtil.userName);

        // convert base64 into
        String PassWord = DBUtil.password;
        //WebElement password = driver.findElement(By.xpath("//input[@id='txt_passwd']"));
        WebElement password = driver.findElement(By.xpath(ATT_Locators.PassWord));
        password.sendKeys(PassWord);

        //txt_station
        WebElement stationName = driver.findElement(By.xpath(ATT_Locators.stationName));
        stationName.sendKeys(DBUtil.stationName);
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

    @Test(priority=2)
    public void dockreceiving() throws SQLException, ClassNotFoundException, InterruptedException {

        /// Calling getSerialNumbetList Method of DBUtil to get serail number and model from the DATA Base
        //getSerialNumberList();
        Thread.sleep(1000);
        ArrayList<String> newTb = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(newTb.get(1));
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.dockReceivingOption)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildDockReceiving)));

        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.dropDownForCarrierInformation)), "FEDEX");
        randomStringForTrackingNumber();
        typeText(driver.findElement(By.xpath(ATT_Locators.trackingNumberField)), mapForTrackingNumber.get("TrackingNumber"));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.savButton)));
        System.out.println("Success msg after dock receiving = " + getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.successMsgOnDockReceiving))));
        String shipmentNbr = getTextValueFromLocator(driver.findElement(By.xpath(ATT_Locators.shipmentReceiveNumber)));
        System.out.println("shipment receive number - " + shipmentNbr);

        mapForShipmentNumber = new LinkedHashMap<>();
        mapForShipmentNumber.put("shipmentNbr", shipmentNbr);
    }

    public void buildGaylord() throws InterruptedException, SQLException, ClassNotFoundException {
        // randonStringforSerialNumber();

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.homeButton))));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordOption)));

        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordButton)));

        typeText(driver.findElement(By.xpath(ATT_Locators.shipmentReceivedTexBox)), mapForShipmentNumber.get("shipmentNbr"));
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.selectDeviceTypeFromDropDown)), "GATEWAY");
        selectOptionFromDropdown(driver.findElement(By.xpath(ATT_Locators.selectItemModelFromDropDown)), DBUtil.getDataFromUserModelMapping());

        clickOnElement(driver.findElement(By.xpath(ATT_Locators.saveButtonOnGaylordPage)));

        Thread.sleep(7000);
        WebElement imgElement = driver.findElement(By.xpath("//img[@id='PrintLabelIcon']"));
        // Get the value of the data-gaylordid attribute
        dataGaylordIdValue = imgElement.getAttribute("data-gaylordid");
        // Print the retrieved value
        System.out.println("data-gaylordid Value: " + dataGaylordIdValue);

    }



    @Test(priority=4)
    public void flowByrMAD() throws SQLException, ClassNotFoundException, InterruptedException {
        //serialnumbers=Arrays.asList("RMADNMILX3YYA");
       serialnumbers= getSerialNumberList();
        //System.out.println("serialnumbers"+serialnumbers);
        mapForSerialNumber =new LinkedHashMap<>();
//        int devices_per_carton=2,cartons_per_pallete=2;
//        factoryReset();
        buildMaterCarton_Pallet(serialnumbers, masterCartonCode);
    }



    public String executeStep(String stepToExecute,String SN,int j,Map<String, String> inputTestDataATT) throws SQLException, InterruptedException, ClassNotFoundException {
        String step;
        String ErrorCode="";


        System.out.println("stepToExecute-----> "+stepToExecute);

        switch (stepToExecute) {
            case "VISUAL_INSPECTION":
                ErrorCode=inputTestDataATT.get("vierrorcode");
                nextStep = visual_Inspection(SN,j,inputTestDataATT,ErrorCode).get("NEXT_STEP_NEW");
                //offsetCountMap.put("visual_Inspection",offsetCountMap.get("visual_Inspection")+1);
                break;
            case "Factory Reset":
                ErrorCode=inputTestDataATT.get("factoryreseterrorcode");
                System.out.println("nextstep at factory reset "+nextStep);
                System.out.println("stepToExecute at factory reset "+stepToExecute);
                nextStep = factoryReset(SN,j,inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                // offsetCountMap.put("Factory_Reset",offsetCountMap.get("Factory_Reset")+1);
                break;
            case "ATTS":
                ErrorCode=inputTestDataATT.get("attserrorcode");
                nextStep=aTTS_Step(SN,inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                // offsetCountMap.put("ATTS_RESET",offsetCountMap.get("ATTS_RESET")+1);
                break;
            case "WIFI":
                ErrorCode=inputTestDataATT.get("wifierrorcode");
                nextStep=wIFI_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                // offsetCountMap.put("WIFI_TEST",offsetCountMap.get("WIFI_TEST")+1);

                break;
            case "ETT":
                ErrorCode=inputTestDataATT.get("etterrorcode");
                nextStep=eTT_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                //  offsetCountMap.put("ETT_TEST",offsetCountMap.get("ETT_TEST")+1);

                break;
            case "ATSG":
                ErrorCode=inputTestDataATT.get("atsgerrorcode");
                nextStep=aTSG_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                // offsetCountMap.put("ATSG_TEST",offsetCountMap.get("ATSG_TEST")+1);

                break;
            case "COSMETIC_CLEAN":
                ErrorCode=inputTestDataATT.get("cosmeticcleanerrorcode");
                nextStep=cOSMETIC_CLEAN_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                //offsetCountMap.put("COSMETIC_CLEAN",offsetCountMap.get("COSMETIC_CLEAN")+1);

                break;
            case "COSMETIC_REPAIR":
                ErrorCode=inputTestDataATT.get("cosmeticrepairerrorcode");
                nextStep=cOSMETIC_REPAIR_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                // offsetCountMap.put("COSMETIC_REPAIR",offsetCountMap.get("COSMETIC_REPAIR")+1);

                break;
            case "KITTING":
                ErrorCode=inputTestDataATT.get("kittingerrorcode");
                nextStep=kITTING_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
                //offsetCountMap.put("KITTING",offsetCountMap.get("KITTING")+1);
                break;
            case "QUALITY_CHECK":
                ErrorCode=inputTestDataATT.get("qcerrorcode");
                nextStep=quality_Check_Step(inputTestDataATT,ErrorCode).get("NEW_NEXT_STEP");
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

        // devices and pallet size should be same
        //


        int count=0;
        for(int i = 0;i<cartons_per_pallet; i++)
        {
           proceesedSerialNumberList =new ArrayList<>();

            for(j = 0;j<units_per_carton; j++)
            {
                //inputData(j,);
                // Map<> serialNumberData=inputData(j,);
                // modelFromModelMapping=mthoed

                inputTestDataATT = getDataFromAttInputTestData(count);

//{serialnumber=AGQAH90NADL3C, qcerrorcode=1100, pcncheck=FALSE, warrrantycheck=NA, cosmeticrepairerrorcode=1100, attserrorcode=1100, wifierrorcode=1100, factoryreseterrorcode=1100, atsgerrorcode=1100, vierrorcode=1100, etterrorcode=1100, model=BGW320-500, id=1, cosmeticcleanerrorcode=1100, kittingerrorcode=1100}

                int viCount = 0;
                nextStep="";
                while (!nextStep.equals("BUILD_CARTON")) {
                    if (viCount == 0) {
                        nextStep = executeStep("VISUAL_INSPECTION",inputTestDataATT.get("serialnumber"), j,inputTestDataATT);
                        System.out.println("serialnumbers.get(j) in while loop"+inputTestDataATT.get("serialnumber"));
                    } else {
                        nextStep = executeStep(nextStep,inputTestDataATT.get("serialnumber"),j,inputTestDataATT);
                        System.out.println("nextStep in while loop "+ nextStep);


                    }
                    viCount++;
                }
                if(units_per_carton == 1){
                    System.out.println("units_per_carton is one");
                }
                else {
                    Thread.sleep(5000);
                    // clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
                    jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.homeButton)));

                    Thread.sleep(2000);
                    clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordOption)));
                    driver.findElement(By.xpath(ATT_Locators.gaylordTextBox)).sendKeys(dataGaylordIdValue);
                    clickOnElement(driver.findElement(By.xpath(ATT_Locators.gaylordSearchbtn)));
                }
                proceesedSerialNumberList.add(inputTestDataATT.get("serialnumber"));

                count++;


            }
            System.out.println("Outside QC");
            Thread.sleep(4000);
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));

            // clickOnElement(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonTab)));
            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild))));
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.masterCartonBuild)));

            wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.masterCartonSNScan))));


            for(int k=0;k<units_per_carton;k++) {
                Thread.sleep(6000);
                System.out.println("@@@@@@@@@@@@@proceesedSerialNumberList"+proceesedSerialNumberList);
                jsExecutor.executeScript("arguments[0].value =;", By.xpath(ATT_Locators.masterCartonSNScan));
                //jsExecutor.executeScript("arguments[0].value = '" +  + "'", inputField)

                driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).sendKeys(proceesedSerialNumberList.get(k));
                System.out.println("Serial number value with variable k "+proceesedSerialNumberList.get(k));
                System.out.println(proceesedSerialNumberList + "serialnumbers in master carton loop");
                Thread.sleep(2000);
                Thread.sleep(2000);
                driver.findElement(By.xpath(ATT_Locators.masterCartonSNScanSearch)).click();


                wait = new WebDriverWait(driver, Duration.ofSeconds(200));
                wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[text()='Device added in master carton successfully']"))));

                if(driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).isEnabled()){

                    driver.findElement(By.xpath(ATT_Locators.masterCartonSNScan)).clear();
                }
                Thread.sleep(2000);

                driver.findElement(By.xpath("//button[@id='mc_code_btn']")).click();
                Thread.sleep(3000);
            }
            Thread.sleep(2000);

            masterCartonNo=driver.findElement(By.xpath(ATT_Locators.masterCartonID)).getText();
            System.out.println("Master carton Number = " + masterCartonNo);
            masterCartonCode.add(masterCartonNo);

            Thread.sleep(2000);
            System.out.println("******MASTER CARTON CODE : "+masterCartonNo);
            Thread.sleep(3000);

            // driver.findElement(By.xpath(ATT_Locators.masterCartonClose)).click();
            jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.masterCartonClose)));

            Thread.sleep(4000);
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
            Thread.sleep(2000);
            clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildGaylordOption)));
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
            Thread.sleep(5000);
            Thread.sleep(2000);
            driver.findElement(By.xpath(ATT_Locators.masterCartonNoSearch)).sendKeys(masterCartonCode.get(l));
            System.out.println("masterCartonCode"+masterCartonCode);
            Thread.sleep(2000);
            driver.findElement(By.xpath(ATT_Locators.palletMasterCartonScanSearch)).click();
            Thread.sleep(2000);
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

        DBUtil.validateINVA(inputTestDataATT.get("serialnumber"));
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

    public Map<String, String> visual_Inspection (String SN, int j, Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {



//Specify the timout of the wait
//        fluentwait = new FluentWait(driver);
//        fluentwait.withTimeout(Duration.ofSeconds(5000));
////Sepcify polling time
//        fluentwait.pollingEvery(Duration.ofSeconds(250));
////Specify what exceptions to ignore
//        fluentwait.ignoring(NoSuchElementException.class);
//
//        fluentwait.until(ExpectedConditions.elementToBeClickable((By.xpath(ATT_Locators.serialNumberTextBox))));



        jsExecutor = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBox))));


        typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBox)), inputTestDataATT.get("serialnumber"));
        System.out.println("Serial number send in the text box");
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButton)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.visualInspectionQuestionText)));

      //  fluentwait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath(ATT_Locators.visualInspectionQuestionText))));


        System.out.println("click on the search button");

        Map<String, String> dataForVisualInspection = DBUtil.getDataFromRuleDB("tblvisualinspection", "Visual_Inspection",DBUtil.modelFromTransactiontbl,inputTestDataATT, errorCode); //offsetCountMap);
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
        DBUtil.validateRMAR(inputTestDataATT.get("serialnumber"));
        //nextSteps.add(DBUtil.nextStepOnVI);
        //step=DBUtil.nextStepOnVI;
        // return dataForVisualInspection.get("NEXT_STEP_NEW");
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.assetTraceability)));

        typeText(driver.findElement(By.xpath(ATT_Locators.searchBoxOnAssetTraceability)),SN);
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

    public Map<String, String> factoryReset (String SN, int j, Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForFactoryReset = DBUtil.getDataFromRuleDB("tblfactoryreset", "Factory_Reset",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode); // offsetCountMap);

        System.out.println("serial number in wip screen " + inputTestDataATT.get("serialnumber"));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.homeButton)));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.wipScreenOption)));
        typeText(driver.findElement(By.xpath(ATT_Locators.serialNumberTextBoxOnWIP)),  inputTestDataATT.get("serialnumber"));
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButtonOnWipScreen)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.factoryResetQuestionText)));


        String result = dataForFactoryReset.get("Factory_Reset");
//        if (result.equals("Pass")) {
//            Thread.sleep(2000);
//            dynamicLocatorByTextForFactoryReset(result);
//        } else {
//            dynamicLocatorByTextForFactoryReset(result);
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnFactoryReset))));
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeOptionOnFactoryReset)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
        DBUtil.validateINVA(inputTestDataATT.get("serialnumber"));

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

    public Map<String, String> aTTS_Step (String SN, Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForAttsStep = DBUtil.getDataFromRuleDB("tblatts", "ATTS_RESET",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode); // offsetCountMap);

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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
    public Map<String, String> wIFI_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForWifiStep = DBUtil.getDataFromRuleDB("tblwifi", "WIFI_TEST",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
    public Map<String, String> eTT_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForEttStep = DBUtil.getDataFromRuleDB("tblett", "ETT_TEST",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);
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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
    public Map<String, String> aTSG_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForAtsgStep = DBUtil.getDataFromRuleDB("tblatsg", "ATSG_TEST",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
    public Map<String, String> cOSMETIC_CLEAN_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {
        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForCosmeticCleanStep = DBUtil.getDataFromRuleDB("tblcosmeticclean", "COSMETIC_CLEAN",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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
    public Map<String, String> cOSMETIC_REPAIR_Step (Map<String, String> inputTestDataATT, String errorCode) throws SQLException, ClassNotFoundException, InterruptedException {
        Map<String, String> dataForCosmeticRepairStep = DBUtil.getDataFromRuleDB("tblcosmeticrepair", "COSMETIC_REPAIR",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

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
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
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

    public Map<String, String> kITTING_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {
        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForKittingStep = DBUtil.getDataFromRuleDB("tblkitting", "KITTING",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

        randonStringforPSUNumber();
        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNKitting)));
        // click on the clickable SN
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.clickableSN)));
        // type PSU number
        wait = new WebDriverWait(driver, Duration.ofSeconds(150));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.textBoxForPSUNumber)));

        typeText(driver.findElement(By.xpath(ATT_Locators.textBoxForPSUNumber)), mapForPSUlNumber.get("PSUNumber"));
        // click on search button of PSU
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.searchButtonForPSU)));
        Thread.sleep(3000);

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
//            Thread.sleep(4000);
//            dynamicLocatorByTextForKitting(result);
//        } else {
//            dynamicLocatorByTextForKitting(result);
//            clickOnElement(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting)));
//
//            wait = new WebDriverWait(driver, Duration.ofSeconds(200));
//            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting))));
//            sendKeysUsingJavaScriptExecutor(driver, driver.findElement(By.xpath(ATT_Locators.errorCodeTextBoxOnKitting)), "3100");
//
//            actions = new Actions(driver);
//            actions.sendKeys(Keys.ENTER).perform();
//            Thread.sleep(2000);
//            actions.sendKeys(Keys.ENTER).perform();
//        }
        // submit the kitting step
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


    public Map<String, String> quality_Check_Step (Map<String, String> inputTestDataATT, String errorCode) throws InterruptedException, SQLException, ClassNotFoundException {

        jsExecutor = (JavascriptExecutor) driver;
        Map<String, String> dataForQualityCheckStep = DBUtil.getDataFromRuleDB("tblqualitycheck", "QUALITY_CHECK",DBUtil.modelFromTransactiontbl,inputTestDataATT,errorCode);// offsetCountMap);

        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.clickableSNQC)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.clickableSN)));

        wait = new WebDriverWait(driver, Duration.ofSeconds(250));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.qCQuestionText)));

        String result = dataForQualityCheckStep.get("QUALITY_CHECK");
//        if (result.equals("Pass")) {
//
//            //dynamicLocatorByTextForQC(result);
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
        Thread.sleep(1000);
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.receivingHomeButton)));
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnTab)));
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.buildAsnButton)));
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnSelectLocation)));
        Thread.sleep(1000);
        typeText(driver.findElement(By.xpath(ATT_Locators.asnSelectLocation)),"FDA:TX");
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnSaveLocation)));
//        for(int i= 0; i<pallet_Id_Created.size(); i++){
        Thread.sleep(2000);
        Thread.sleep(2000);
        System.out.println("Pallet code in ASN" + palletCodefromDb);
//        jsExecutor.executeScript("arguments[0].value = arguments[1];", driver.findElement(By.xpath(ATT_Locators.palletCodeOnAsn)),palletCodefromDb);
        wait = new WebDriverWait(driver, Duration.ofSeconds(600));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ATT_Locators.palletCodeOnAsn)));

        typeText(driver.findElement(By.xpath(ATT_Locators.palletCodeOnAsn)),palletCodefromDb);
        Thread.sleep(1000);
        Thread.sleep(1000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.palletCodeBtnOnAsn)));
        Thread.sleep(3000);
//        }

//        Thread.sleep(2000);
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.addShippingDateTimePopupBtn)));
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.addShippingDateTimePopupBtn)));
        Thread.sleep(2000);

//        WebElement dateBox = driver.findElement(By.xpath(ATT_Locators.calenderselectDate));
//        dateBox.sendKeys("03202024");


        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.calenderselectDate)));

        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.calenderselectDate)));
        typeText(driver.findElement(By.xpath(ATT_Locators.shippingDatePopupOnAsn)),"11/21/2024");
        Thread.sleep(2000);
//        clickOnElement(driver.findElement(By.xpath(ATT_Locators.shippingTimePopupOnAsn)));
        Thread.sleep(2000);
        //  clickOnElement(driver.findElement(By.xpath(ATT_Locators.calenderselectTime)));
        jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.xpath(ATT_Locators.calenderselectTime)));

        clickOnElement(driver.findElement(By.xpath("//span[text()='1']")));  // click on 1 pm
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath("//span[@class='mdtp__button ok']"))); // click on Ok button
        Thread.sleep(2000);
//        typeText(driver.findElement(By.xpath(ATT_Locators.shippingTimePopupOnAsn)),"10:06:22PM");



        wait = new WebDriverWait(driver, Duration.ofSeconds(200));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(ATT_Locators.saveButtonPopupOnAsn))));
        // clickOnElement(driver.findElement(By.xpath(ATT_Locators.saveButtonPopupOnAsn)));
        Thread.sleep(2000);
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.asnClose)));
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.areUSureASNClosePopup)));
        Thread.sleep(2000);
        Thread.sleep(2000);
        // call inva
        DBUtil.validateINVA(inputTestDataATT.get("serialnumber"));

        if(DBUtil.from_location!=null && DBUtil.to_location !=null && DBUtil.from_location.equalsIgnoreCase("RVAR") && DBUtil.to_location.equalsIgnoreCase("Bulk"))
        {
            System.out.println("From location on ASN screen "+DBUtil.from_location+ ", To location on  ASN screen "+DBUtil.to_location);
            System.out.println("From location and To location is as Expected on  ASN screen");
        }
        else
        {
            System.out.println("From location and To location is not as Expected on  ASN screen");
        }
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.shipASNButton)));
        Thread.sleep(2000);
        typeText(driver.findElement(By.xpath(ATT_Locators.addTrackingNoPopup)),"3456789012345");
        Thread.sleep(2000);
        clickOnElement(driver.findElement(By.xpath(ATT_Locators.addTrackingNoSaveBtn)));

    }*/
}
