package att_Jenkins;

public class ATT_Locators {


    public static final String username="//input[@id='txt_username']";
    public static final String PassWord="//input[@id='txt_passwd']";
    public static final String stationName ="//input[@id='txt_station']";
    public static final String receivingTile ="//h3[normalize-space()='Receiving Activity by ASN']";
    public static final String tmmOrgTile="//h3[text()='TMM']";
    public static final String attOrgTile="//h3[text()='Gateway']";
    public static final String sqOrgTile="//h3[text()='SEQUOIA']";
    public static final String tavOrgTile="//h3[text()='TAV']";
    public static final String parentField ="//input[@id='search_number']";
    public static final String SearchButtonOfCheckIn ="(//button[@type='submit'])[2]";
    public static final String serialNumberSearchBoxOnCheckIn ="//input[@id='search_number_sn']";
    public static final String questionnairesPage ="(//div[@class='row'])[2]";

    public static final String submitButtonOfQuestionnaires ="(//button[text()='Submit'])[1]";

    public static final String homeButton ="//a[@title='Home']";
    public static final String receivingHomeButton="//a[contains(text(),'Home')]";

    //  new locatores
    public static final String assetTraceability="//span[text()='Asset Traceability']";
    public static final String searchBoxOnAssetTraceability="//input[@id='serial_number']";
    public static final String clickOnSearchButton="//button[@type='submit']";
    public static final String nextStepOnAssetTraceability="//h5[@id='nextStep_id']";

    public static final String actualNextStepOnWip="//div[@class='col-md-6 col-sm-6 next-step-div']";

//------------------------------------------------------------------------------


    public static final String dockReceivingOption="//h3[text()='Dock Receiving']";
    //  public static final String dockReceivingOption="//span[text()='Dock Receiving']";

    public static final String buildDockReceiving="//button[@id='buildShipment']";

    public static final String dropDownForCarrierInformation="//select[@id='carrier']";

    public static final String trackingNumberField="//input[@id='trackingNumber']";
    public static final String savButton="//button[text()='Save']";
    public static final String successMsgOnDockReceiving="//div[text()='Shipment Built Successfully']";
    public static final String shipmentReceiveNumber="//td[@class='sorting_1']";

    // ---------------------------------------- Build Gaylord----------------------------------------------------//

    public static final String buildGaylordOption="buildGaylord";
    public static final String buildGaylordButton="//button[text()='Build Gaylord']";
    public static final String buildGaylordBlock="//h3[normalize-space(text()) = 'Build Gaylord']";
    public static final String shipmentReceivedTexBox="//input[@id='shipment-receive-rlc']";

    public static final String selectDeviceTypeFromDropDown="//select[@id='device-type-rlc-shipment']";
    public static final String selectItemModelFromDropDown="//select[@id='item-model-rlc-shipment']";
    public static final String saveButtonOnGaylordPage="(//button[text()='Save'])[2]";
    public static final String gaylordCode="//input[@id='gaylord-code']";
    public static final String serialNumberTextBox="//input[@id='serial-number']";

    public static final String searchButton="//button[@id='serial-number_btn']";

    public static final String warningPopUp="(//div[@class='form-group']//h5)[1]";
    public static final String answerAsYes="(//button[text()='Yes'])[2]";
    public static final String itemNumber="//select[@id='item-number-new']";
    public static final String nextButton="//button[text()='Next']";
    public static final String successMsgAfterBlindReceipt="//div[text()='Blind receipt successfully performed']";
    public static final String closegaylord="closeGaylord";

    //--------------------------------- Visual Inspection ----------------------------------------------------//


    public static final String error_CodeOnVI ="//span[@id='select2-error-code-details-container']";
    public static final String errorCodeTextBox="//input[@class='select2-search__field']";
    public static final String submitVI="(//div[@class='pop_up_modeal']//div//div//button)[2]";
    public static final String successMsgOnVisualInspection="//div[text()='Visual Inspection Done Successfully']";

    //------------------------------- WIP Screen ---------------------------------------------------//
    public static final String wipScreenOption="//span[text()='WIP Screen']";
    public static final String serialNumberTextBoxOnWIP="//input[@id='serial-number']";
    public static final String searchButtonOnWipScreen="//button[@id='serial-number_btn']";

//------------------------------------------------------------------//

    public static final String clickableSNCosmeticClean ="//*[text()=' : COSMETIC_CLEAN']";
    public static final String clickableSNATTs ="//*[text()=' : ATTS']";
    public static final String clickableSNWIFI ="//*[text()=' : WIFI']";
    public static final String clickableSNETT ="//*[text()=' : ETT']";
    public static final String clickableSNATSG="//*[text()=' : ATSG']";
    public static final String clickableSNComseticClean="//*[text()=' : COSMETIC_CLEAN']";

    public static final String clickableSNComseticRepair="//*[text()=' : COSMETIC_REPAIR']";
    public static final String clickableSNKitting="//*[text()=' : KITTING']";
    public static final String clickableSNQC="//*[text()=' : QUALITY_CHECK']";


    public static final String visualInspectionQuestionText="//label[text()='Is the device visually inspected? ']";
    public static final String factoryResetQuestionText="//span[text()='Was the device reset to its factory defaults?']";
    public static final String aTTQuestionText="//span[text()='Did the device successfully passed the ATTS test?']";
    public static final String wIFIQuestionText="//span[text()='Did the device successfully passed the Wifi test?']";
    public static final String eTTQuestionText="//span[text()='Did the device successfully passed the ETT?']";
    public static final String aTSGQuestionText ="//span[text()='Did the device successfully passed the ATSG?']";
    public static final String cosmeticCleanQuestionText="//label[text()='Is the device clean? ']";

    public static final String cosmeticRepairQuestionText="//label[text()='Is the device cosmetic repaired?']";

    public static final String kittingQuestionText="//label[text()='Is Kitting done for this device?']";
    public static final String qCQuestionText="//label[text()='Is the device quality checked?']";



    //-------------------------------------Error code ---------------------------------------------------
    public static final String errorCodeOptionOnFactoryReset ="(//ul[@class='select2-selection__rendered'])[1]";

    public static final String errorCodeTextBoxOnFactoryReset="(//input[@class='select2-search__field'])[1]";

    public static final String clickOnErrorCode="//li[contains(text(),'2610')]";
    public static final String errorCodeTextBoxOnCosmeticClean="(//ul[@class='select2-selection__rendered'])[2]";
    public static final String errorCodeTextBoxOnKitting="(//ul[@class='select2-selection__rendered'])[3]";
    public static final String errorCodeTextBoxOnCosmeticRepair= "(//ul[@class='select2-selection__rendered'])[3]";

    public static final String errorCodeTextBoxOnQC= "(//ul[@class='select2-selection__rendered'])[5]";


    //------------------------------ Factory Reset ------------------------------//

    // this xpath for submit button is applicable for factory reset,ATTS,WIFI,ETT,ATSG
    public static final String submitButtonOnFactoryReset ="//button[@id='save']";
    public static final String submitButtonOnCosmeticClean ="//button[@id='save-btn_cosmetic_clean']";
    public static final String submitButtonOnCosmeticRepair="//button[@id='save_cosmetic_repair']";
    //  public static final String submitButtonOnKitting ="//button[@id='save_kitting']";
    public static final String submitButtonForKitting="//button[@id='save_kitting']";
    public static final String submitButtonQualityCheck="//button[@id='save_QC']";
    public static final String clickableSN="//a[@class='clickable-Sn']";

    //public static final String clickableSNWIFI="/html/body/section/div/section/div/section/div/div/section/div/div[1]/div/div/div[3]/h5/text()[2]";

    //--------------------------- kitting -----------------------------------------------

    public static final String textBoxForPSUNumber="//input[@id='psuNumber_kitting']";
    public static final String searchButtonForPSU="//button[@id='psuNumberBtn_kitting']";
    public static final String asnwerAsYesForPSUPopUp="//button[@id='yes-warning-psu-number-btn']";
    //(//button[text()='Yes'])[2]

    public static final String warningPop="(//h4[text()='Warning'])[2]";
    public static final String closeWarningPopUp="(//button[@class='close'])[12]";



    //------------------------------------- Master Carton -------------------------------

    public static final String masterCartonOptionFromleftSideBar="//a[@title='Master Carton']";
    public static final String buildMasterCarton="//button[@id='buildMc']";

    public static final String qcSearchSN = "//input[@id='serial-number']";
    public static final String masterCartonTab = "//h3[contains(text(),'Master Carton')]";
    public static final String masterCartonBuild = "//button[@id='buildMc']";
    public static final String masterCartonNoSearch = "//input[@id='mc_code']";
    public static final String masterCartonSNScan = "//input[@id='serial_number']";
    public static final String masterCartonSNScanSearch = "//button[@id='serial_number_btn']";
    public static final String masterCartonClose = "//button[@id='closeCarton']";
    public static final String masterCartonText = "//*[text()=' : BUILD_CARTON']";
    public static final String masterCartonIssueText = "Master carton contain more than one device";

    //=====================================================================================



    public static final String gaylordTextBox= "//input[@id='gaylord-code']" ;
    public static final String gaylordSearchbtn ="//button[@id='gaylord-code_btn']";

    //------------------------------- WIP Screen ---------------------------------------------------//


    public static final String masterCartonID ="/html[1]/body[1]/section[1]/div[1]/section[1]/div[1]/section[1]/div[1]/div[1]/section[1]/div[1]/div[4]/div[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[4]/span[1]";

    //------------------------------------- Pallet -------------------------------
    public static final String palletTab = "//h3[contains(text(),'Pallet')]";
    public static final String buildPalletButton = "//button[@id='buildPallet']";
    public static final String palletCode = "//input[@id='pallet_code']";
    public static final String palletCodeBtn = "//button[@id='pallet_code_btn']";
    public static final String palletMasterCartonScanSearch ="//button[@id='mc_code_btn']";
    public static final String palletClose = "//button[@id='closePallet']";
    public static final String areUSurePalletClosePopup = "//button[@id='yes-pallet-closing-btn']";




    //------------------------------------- ASN -------------------------------
    public static final String asnTab = "//h3[contains(text(),'ASN')]";
    public static final String buildAsnButton = "//button[@id='buildASN']";
    public static final String asnSelectLocation = "//select[@id='add-location']";
    public static final String asnSaveLocation = "//button[@id='save-location']";
    public static final String asnClose = "//button[@id='closeASN']";
    public static final String addShippingDateTimePopupBtn = "//button[@id='Add-shipping-date']";
    public static final String palletCodeOnAsn = "//input[@id='pallet-code']";
    public static final String palletCodeBtnOnAsn = "//button[@id='pallet-code_btn']";

    public static final String shippingDatePopupOnAsn = "//input[@id='shipping-date']";
    public static final String shippingTimePopupOnAsn = "//input[@id='shipping-time']";
    public static final String saveButtonPopupOnAsn = "//button[@id='save-date']";

    public static final String areUSureASNClosePopup = "//button[@id='yes-ASN-closing-btn']";

    public static final String calenderselectDate ="//div[@class='input-group-addon']";
    public static final String calenderselectTime= "//div[@class='input-group-addon timepicker']";

    public static final String shipASNButton = "//button[@id='ship-ASN']";
    public static final String addTrackingNoPopup = "//input[@id='tracking-number']";
    public static final String addTrackingNoSaveBtn = "//button[@id='save-tracking-number']";




}
