package utilityPackage;

// import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.*;

import static java.lang.Integer.parseInt;

public class DBUtil {
    public static String orgid = ReadDataFromPropertyFile.data.getProperty("orgId");
    public static String orgNameFormPropertyFile = ReadDataFromPropertyFile.data.getProperty("orgName");
    public static String valueOfIsSanity;
    public static String passwordIntoBase64;


    public static int org_id = parseInt(orgid);
    public static String orgname;
    public static String stationName;
    public static String userName;
    public static String password;
    //public static String OrgId;
    public static String flowOfsequence;
    public static String testCaseName;
    public static String configids;
    public static String SerialNumber;
    public static String modelFromTransactiontbl ="";
    public static String modelFromRuleDB;
    public static  String resultOfVIStep;
    public static String nextStepOnVI;
    public static String sku="";
    public static String  unitesPerCarton;
    public static String  unitesPerPallet;
    public static String palletId;

    public static String from_location;
    public static String to_location;
    public static ArrayList<Map<String, String>> objListOfConfigData = new ArrayList<>();

    public static ArrayList<String> objflows = new ArrayList<String>();

    public static ArrayList<Map<String, String>> objflowsMaps = new ArrayList<>();
    public static Connection conn;


    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        getOrgInfo();
//        getFlowsFromDataBase();
//        //getQuestionnairesList();
//        getProviderData();
       // getSerialNumberList();
        //getDataFromRuleDB("tblvisualinspection", "Visual_Inspection","BGW210-700",);
        getCartonPalletCount();
//        validateRMAR("AGQAYXXQXJDB2");
//        validateINVA("R98765678909877");
//        validateASNAcknowledge("ASN20240321002");
//        getDataFromAttInputTestData(0);
//        getDataFromUserModelMapping();

        getMasterCartonId("JJJQBI8V4S3ZS");

    }

    public static DBUtil getOrgInfo() throws ClassNotFoundException, SQLException  {
        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

        System.out.println("Connection success");
        Statement stat = conn.createStatement();


        String queryToGetDataFromOrg_Master = DBConstant.Query_For_org_master;
        // Create a prepared statement
        preparedStatementTest = conn.prepareStatement(queryToGetDataFromOrg_Master);
        // Set parameters (if needed)
        preparedStatementTest.setInt(1, org_id);
        // Execute the queryToGetDataFromOrg_Master

        try {

            resultSet = preparedStatementTest.executeQuery();


            // Process the ResultSet
            while (resultSet.next()) {
                ResultSetMetaData rsmd = resultSet.getMetaData();

                String parentfieldname = resultSet.getString(8);
                System.out.println("parentfieldname = " + parentfieldname);

                stationName = resultSet.getString(5);
                System.out.println("stationName = " + stationName);

//                OrgId = resultSet.getString(2);
//                System.out.println("OrgId = " + OrgId);

                userName = resultSet.getString(6);
                System.out.println("userName = " + userName);

                password = resultSet.getString(7);
                System.out.println("password = " + password);

                passwordIntoBase64 = encodeToBase64(password);
                System.out.println("encodeToBase64(password) "+  passwordIntoBase64);


                orgname = resultSet.getString(3);

                int orgid = resultSet.getInt(2);
                System.out.println(orgname + "," + orgid);

                objDBUtil = new DBUtil();

                objDBUtil.orgname = orgname;
            }


        } catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return objDBUtil;

    }

    public static ArrayList<Map<String, String>> getFlowsFromDataBase() throws SQLException, ClassNotFoundException {

        // Fetch the value for the isSanity Flag from property file
        valueOfIsSanity = ReadDataFromPropertyFile.data.getProperty("Is_sanity");

        boolean booleanValueOfIsSanity = Boolean.parseBoolean(valueOfIsSanity);
        System.out.println("valueOfIsSanity"+valueOfIsSanity);
        //valueOfIsSanity!= null

        if (booleanValueOfIsSanity==true) {
            // Convert the string to boolean
            boolean valueForIsSanityFlag = Boolean.parseBoolean(valueOfIsSanity);
            System.out.println("valueForIsSanityFlag--->" + valueForIsSanityFlag);


            Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
            conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                    DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

            System.out.println("Connection success");
            Statement stat = conn.createStatement();

            DBUtil objDBUtil = null;
            PreparedStatement preparedStatementTest = null;
            ResultSet resultSet = null;

            // String queryToGetDataFromTestCase_Master = DBConstant.Query_For_testcase_master;

            String queryToGetDataFromTestCase_Master = DBConstant.fetchTestCaseWithSanityValue;
            preparedStatementTest = conn.prepareStatement(queryToGetDataFromTestCase_Master);
            // Set parameters (if needed)
            preparedStatementTest.setInt(1, Integer.parseInt(orgid));
            preparedStatementTest.setBoolean(2,valueForIsSanityFlag);


//        List<String> list = Arrays.asList(ReadDataFromPropertyFile.data.getProperty("TestCases").split(","));
//        for (int i = 0; i < list.size(); i++) {
//          preparedStatementTest.setString(i + 2, list.get(i));
//       }
            try {

                resultSet = preparedStatementTest.executeQuery();
                // Process the ResultSet
                while (resultSet.next()) {

                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    String testCaseId = resultSet.getString(2);
                    System.out.println("TestCase from DB " + testCaseId);
                    testCaseName = resultSet.getString(3);
                    System.out.println("TestCaseName  " + testCaseName);
                    flowOfsequence = resultSet.getString(5);
                    System.out.println("flowOfsequence = " + flowOfsequence);



//                configids = resultSet.getString(7);
//                System.out.println("configid from testCaseMaster = " + configids);
//                String[] configid = configids.split(",");
//                String queryForConfigurationMaster = DBConstant.queryForConfigurationMaster;
//                preparedStatementTest = conn.prepareStatement(queryForConfigurationMaster);
//
//                for (int k = 0; k < configid.length; k++) {
//                    // Set parameters
//                    preparedStatementTest.setString(1, configid[k]);
//                    // Execute the prepared statement
//                    resultSet = preparedStatementTest.executeQuery();
//
//
//                    while (resultSet.next()) {
//                        String config_id = resultSet.getString(2);
//                       // System.out.println(config_id);
//                        String config_key = resultSet.getString(3);
//                        //System.out.println(config_key);
//                        String config_value = resultSet.getString(4);
//                        //System.out.println(config_value);
//                        String activityname = resultSet.getString(5);
//                       // System.out.println(activityname);
//
//                        Map<String, String> objConfigData = new LinkedHashMap<>();
//                        objConfigData.put("configid", config_id);
//                        objConfigData.put("configkey", config_key);
//                        objConfigData.put("configvalue", config_value);
//                        objConfigData.put("activityname", activityname);
//
//                        objListOfConfigData.add(objConfigData);
//
//
//
//
//                    }
//                }
//
//                System.out.println("ConfigData for " + testCaseId + "-->" + objListOfConfigData);

                    Map<String, String> objMapforFlow = new HashMap<>();
                    objMapforFlow.put("TestCase", testCaseId);
                    objMapforFlow.put("TestCaseName", testCaseName);
                    objMapforFlow.put("flowOfsequence", flowOfsequence);
                    objflowsMaps.add(objMapforFlow);
                    //System.out.println("===================" + objMapforFlow.get("TestCase"));
                }
            } catch (SQLException e) {
                // Handle the exception
                System.out.println("there might some issue");
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }

            }

        }
        // to run TestCase with Regression
        else if(booleanValueOfIsSanity==false) {
            // Convert the string to boolean

            Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
            conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                    DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

            System.out.println("Connection success");
            Statement stat = conn.createStatement();
            DBUtil objDBUtil = null;
            PreparedStatement preparedStatementTest = null;
            ResultSet resultSet = null;

            // String queryToGetDataFromTestCase_Master = DBConstant.Query_For_testcase_master;

            String queryToGetDataFromTestCase_Master = DBConstant.fetchTestCaseWithRegression;
            preparedStatementTest = conn.prepareStatement(queryToGetDataFromTestCase_Master);
            // Set parameters (if needed)
            preparedStatementTest.setInt(1, Integer.parseInt(orgid));



//        List<String> list = Arrays.asList(ReadDataFromPropertyFile.data.getProperty("TestCases").split(","));
//        for (int i = 0; i < list.size(); i++) {
//          preparedStatementTest.setString(i + 2, list.get(i));
//       }
            try {

                resultSet = preparedStatementTest.executeQuery();
                // Process the ResultSet
                while (resultSet.next()) {

                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    String testCaseId = resultSet.getString(2);
                    System.out.println("TestCase from DB " + testCaseId);
                    testCaseName = resultSet.getString(3);
                    System.out.println("TestCaseName  " + testCaseName);
                    flowOfsequence = resultSet.getString(5);
                    System.out.println("flowOfsequence = " + flowOfsequence);

                    // put testCaseId,testCaseName, flowOfsequence in the map
                    Map<String, String> objMapforFlow = new HashMap<>();
                    objMapforFlow.put("TestCase", testCaseId);
                    objMapforFlow.put("TestCaseName", testCaseName);
                    objMapforFlow.put("flowOfsequence", flowOfsequence);
                    objflowsMaps.add(objMapforFlow);
                    //System.out.println("===================" + objMapforFlow.get("TestCase"));
                }
            } catch (SQLException e) {
                // Handle the exception
                System.out.println("there might some issue");
                e.printStackTrace();

            } finally {
                if (conn != null) {
                    conn.close();
                }

            }

        }

        return objflowsMaps;
    }


    /*public static ArrayList<Map<String, Object>> getQuestionnairesList () throws
            SQLException, ClassNotFoundException {


        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_receving,
                DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

        System.out.println("Connection success");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataOfQuestionnairesFromDB = DBConstant.queryForQuestionnairesList;
        preparedStatementTest = conn.prepareStatement(dataOfQuestionnairesFromDB);
        // Set parameters (if needed)
        preparedStatementTest.setInt(1, Integer.parseInt(orgid));
        preparedStatementTest.setString(2, orgNameFormPropertyFile + "_QUESTIONNAIRE");


        ArrayList<Map<String, Object>> questionnaireArray = null;
        try {

            questionnaireArray = new ArrayList<>();
            resultSet = preparedStatementTest.executeQuery();


            // Process the ResultSet
            while (resultSet.next()) {

                Map<String, Object> questionnairesJSONPayload = new HashMap<String, Object>();
                questionnairesJSONPayload.put("questionId", resultSet.getInt(1));
                questionnairesJSONPayload.put("questionCode", resultSet.getString(2));
                questionnairesJSONPayload.put("questionText", resultSet.getString(3));
                questionnairesJSONPayload.put("responseId", 42);
                questionnairesJSONPayload.put("responseValue", "No");

                questionnaireArray.add(questionnairesJSONPayload);


            }

            // System.out.println("DBArray ---------------> "+questionnaireArray);
            // System.out.println("Excel reading ----================----------------");
            //System.out.println("ExcelArray =============="+ReadQuestionnairesDataFromExcel.questionnairesQuestions().toString());
//
            List<Map<String, Object>> ExcelQuestionnairesData = ReadQuestionnairesDataFromExcel.questionnairesQuestions();
            List<Map<String, Object>> finalListOfQuestionnaires = new ArrayList<>();

            for (Map<String, Object> questionFromDB : questionnaireArray) {
                for (Map<String, Object> questionFromExcel : ExcelQuestionnairesData) {

                    if (questionFromDB.get("questionCode").toString().equals(questionFromExcel.get("questioncode").toString()) &&
                            questionFromExcel.containsKey("responseValue") && questionFromExcel.get("responseValue").equals("Yes")) {
                        questionFromDB.put("responseValue", "Yes");

                        finalListOfQuestionnaires.add(questionFromDB);
                    } else if (questionFromDB.get("questionCode").toString().equals(questionFromExcel.get("questioncode").toString())) {

                        finalListOfQuestionnaires.add(questionFromDB);

                    }

                }

            }
            System.out.println("Final list ----->" + finalListOfQuestionnaires);

        } catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();

        } finally {
            if (conn != null) {
                conn.close();
            }

            return questionnaireArray;

        }

    }*/

    // public static JSONObject getProviderData () throws ClassNotFoundException, SQLException {

    //     JSONObject objjsonObject = null;
    //     Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
    //     conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
    //             DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

    //     System.out.println("Connected with DB for getProviderData Method");
    //     Statement stat = conn.createStatement();

    //     DBUtil objDBUtil = null;
    //     PreparedStatement preparedStatementTest = null;
    //     ResultSet resultSet = null;

    //     String dataOfQuestionnairesFromDB = DBConstant.queryForOrgMasterToFetchProviderData;
    //     preparedStatementTest = conn.prepareStatement(dataOfQuestionnairesFromDB);
    //     // Set parameters (if needed)
    //     preparedStatementTest.setInt(1, Integer.parseInt(orgid));

    //     try {

    //         resultSet = preparedStatementTest.executeQuery();

    //         // Process the ResultSet
    //         while (resultSet.next()) {

    //             String csi_provider = resultSet.getString(12);
    //             System.out.println("csi_provider--->" + csi_provider);

    //             String csi_provider_json = resultSet.getString(13);
    //             System.out.println("csi_provider_json--->" + csi_provider_json);

    //             objjsonObject = new JSONObject(csi_provider_json);
    //             System.out.println("csi_provider in jsonObject--->" + objjsonObject.toString());


    //         }
    //     } catch (SQLException e) {
    //         // Handle the exception
    //         System.out.println("there might some issue");
    //         e.printStackTrace();

    //     } finally {
    //         if (conn != null) {
    //             conn.close();
    //         }
    //     }


    //     return objjsonObject;
    // }

    public static List<String> getSerialNumberList () throws ClassNotFoundException, SQLException {
        List<String> sourceSerialNumber = new ArrayList<>();
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connected with DB for getSerialNumberList Method");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String serialNumberFromDB = DBConstant.queryToGetSerialNumberList;
        preparedStatementTest = conn.prepareStatement(serialNumberFromDB);


        try {

            resultSet = preparedStatementTest.executeQuery();


            // Process the ResultSet
            while (resultSet.next()) {

                SerialNumber =resultSet.getString(4);
                sourceSerialNumber.add(SerialNumber);

                modelFromTransactiontbl =resultSet.getString(3);
                // sourceSerialNumber.add(modelFromTransactiontbl);

                sku =resultSet.getString(2);
                // sourceSerialNumber.add(sku);

                //System.out.println("sourceSerialNumber--->" + sourceSerialNumber);
                //System.out.println("sourceSerialNumber" + sourceSerialNumber +"model--->" + model);



            }
            System.out.println("sourceSerialNumber" + sourceSerialNumber +"model--->" + modelFromTransactiontbl);
        } catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return sourceSerialNumber;
    }
    public static Map<String, String> getDataFromRuleDB(String tablename, String step, String model) throws ClassNotFoundException, SQLException {

        // parameter that passed in the method -> Map<String,Integer> offset
        //int offsetcount=offset.get(step);
        // Map<String, String> inputTestDataATT = null;
        //  String ErrorCode="1100";



//        if(step.equalsIgnoreCase("Visual_Inspection"))
//        {
           /*  inputTestDataATT = new HashMap<>();
             // Add the key-value pairs to the map
            inputTestDataATT.put("serialnumber", "AGQAH90NADL3C");
            inputTestDataATT.put("qcerrorcode", "1100");
            inputTestDataATT.put("pcncheck", "FALSE");
            inputTestDataATT.put("warrrantycheck", "NA");
            inputTestDataATT.put("cosmeticrepairerrorcode", "1100");
            inputTestDataATT.put("attserrorcode", "1100");
            inputTestDataATT.put("wifierrorcode", "1100");
            inputTestDataATT.put("factoryreseterrorcode", "1100");
            inputTestDataATT.put("atsgerrorcode", "1100");
            inputTestDataATT.put("vierrorcode", "1100");
            inputTestDataATT.put("etterrorcode", "1100");
            inputTestDataATT.put("model", "BGW320-500");
            inputTestDataATT.put("id", "1");
            inputTestDataATT.put("cosmeticcleanerrorcode", "1100");
            inputTestDataATT.put("kittingerrorcode", "1100");*/

        // }

        String queryForVisualInspection="select * from qa_automation_testing."+tablename+" t where \"MODEL\"=? order by \""+step+"\"  desc  limit 1 ";


        // used in the query after limit 1 ---> offset "+offsetcount


        Map<String,String> nextSteps=new HashMap<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatementTest = null;
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

        System.out.println("Connection success");
        Statement stat = conn.createStatement();


        String queryToGetDataForVisualInspection = queryForVisualInspection;
        preparedStatementTest = conn.prepareStatement(queryToGetDataForVisualInspection);
        // Set parameters (if needed)
        preparedStatementTest.setString(1,model);
//        preparedStatementTest.setString(2,inputTestDataATT.get("pcncheck"));
//        preparedStatementTest.setString(3,inputTestDataATT.get("warrrantycheck"));
//        preparedStatementTest.setString(4,errorCode);
//        // Create a prepared statement

        Statement stmt = conn.createStatement();

        resultSet = preparedStatementTest.executeQuery();
        // resultSet=stmt.executeQuery(queryToGetDataForVisualInspection);

        System.out.println("queryToGetDataForVisualInspection"+queryToGetDataForVisualInspection);

        ResultSetMetaData rsmd = resultSet.getMetaData();

        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i)+ "=" + columnValue );

                nextSteps.put(rsmd.getColumnName(i),columnValue);

            }
            System.out.println("nextSteps================>"+nextSteps);

//                resultOfVIStep = resultSet.getString(2);
//                modelFromRuleDB = resultSet.getString(5);
//                nextStepOnVI = resultSet.getString(7);
//
//                nextSteps.add(nextStepOnVI);
//
//                System.out.println("resultOfStep - "+ resultOfVIStep + ",modelFromRuleDB - "+modelFromRuleDB+", nextStepOnVI - "+nextStepOnVI);

        }
        return nextSteps;

    }
    public static void getCartonPalletCount() throws ClassNotFoundException, SQLException {
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success for profile master");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataFromProfileMaster = DBConstant.queryForProfileMaster;
        preparedStatementTest = conn.prepareStatement(dataFromProfileMaster);
        // Set parameters (if needed)
        preparedStatementTest.setInt(1, Integer.parseInt(orgid));
        preparedStatementTest.setString(2,sku );

        resultSet = preparedStatementTest.executeQuery();


        try {
            // Process the ResultSet
            while (resultSet.next()) {

                unitesPerCarton = resultSet.getString(29);
                unitesPerPallet = resultSet.getString(30);
                System.out.println("unitesPerCarton-->" + unitesPerCarton + " ,unitesPerPallet-->" + unitesPerPallet);


            }
        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();

        } finally {
            if (conn != null) {
                conn.close();
            }
        }


    }

    public static String retrievePalletCode(String masterCartonCode) throws ClassNotFoundException, SQLException {
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success for tblmstpallet");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataFromtblmstpallet = DBConstant.queryToGetpalletId;
        preparedStatementTest = conn.prepareStatement(dataFromtblmstpallet);
        // Set parameters (if needed)
        preparedStatementTest.setString(1, masterCartonCode);

        resultSet = preparedStatementTest.executeQuery();
        //System.out.println("RESULTSET PALLETE NO -"+resultSet.getString(2));


        try {
            System.out.println("INSIDE PALLETE TRY BLOCK");
            // Process the ResultSet
            while (resultSet.next()) {
                palletId = resultSet.getString(2);
                System.out.println("palletId-->" + palletId );
            }
        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return palletId;
    }

    public static  void validateRMAR(String serialNumber) throws ClassNotFoundException, SQLException {

        // String valueOf_Is_Send_Flag="";
        int countOfRMAR = 0;

        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success for RMAR generation");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataForRMAR = DBConstant.queryForRMARValidation;
        preparedStatementTest = conn.prepareStatement(dataForRMAR);
        // Set parameters (if needed)
        preparedStatementTest.setString(1, serialNumber);

        resultSet = preparedStatementTest.executeQuery();


        try {
            // Process the ResultSet
            while (resultSet.next()) {
                System.out.println("inside the while loop of RMAR");
//                valueOf_Is_Send_Flag = resultSet.getString(19);
//                System.out.println(" value of Is send Flag for RMAR --> " + valueOf_Is_Send_Flag);

                countOfRMAR = resultSet.getInt(1);
                System.out.println(" count for RMAR --> " + countOfRMAR);

            }
//            if(valueOf_Is_Send_Flag.equalsIgnoreCase("t"))
//            {
//                System.out.println("RMAR generated successfully");
//            }
//            else
//            {
//                System.out.println("RMAR not generated");
//            }
            if(countOfRMAR>=1)
            {
                System.out.println("RMAR generated successfully");
            }
            else
            {
                System.out.println("RMAR not generated");
            }
        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static  void validateINVA(String serialNumber) throws ClassNotFoundException, SQLException {

        String isSendFlagOfINVA="";
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success for INVA generation");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataForINVA = DBConstant.queryForINVAValidation;
        preparedStatementTest = conn.prepareStatement(dataForINVA);
        // Set parameters (if needed)
        preparedStatementTest.setString(1, serialNumber);

        resultSet = preparedStatementTest.executeQuery();


        try {
            // Process the ResultSet
            while (resultSet.next()) {
                from_location = resultSet.getString(6);
                System.out.println(" value of from_location in INVA --> " + from_location);

                to_location = resultSet.getString(7);
                System.out.println(" value of to_location in INVA --> " + to_location);

                isSendFlagOfINVA = resultSet.getString(18);
                System.out.println(" value of to_location in INVA --> " + isSendFlagOfINVA);

            }

            if(isSendFlagOfINVA.equalsIgnoreCase("t"))
            {
                System.out.println("Inva generated successfully");
            }
            else
            {
                System.out.println("Inva not generated");
            }

        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue in INVA generation");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static  void validateASNAcknowledge(String ASN) throws ClassNotFoundException, SQLException {

        String isSendFlagForASN="";
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success for asn_header");
        Statement stat = conn.createStatement();

        DBUtil objDBUtil = null;
        PreparedStatement preparedStatementTest = null;
        ResultSet resultSet = null;

        String dataForASNAck = DBConstant.queryForASN;
        preparedStatementTest = conn.prepareStatement(dataForASNAck);
        // Set parameters (if needed)
        preparedStatementTest.setString(1, ASN);

        resultSet = preparedStatementTest.executeQuery();


        try {
            // Process the ResultSet
            while (resultSet.next()) {
                isSendFlagForASN = resultSet.getString(17);
                System.out.println(" value of from_location in ASN --> " + isSendFlagForASN);
            }

            if(isSendFlagForASN.equalsIgnoreCase("t"))
            {
                System.out.println("ASN acknowledge send successfully");
            }
            else
            {
                System.out.println("ASN acknowledge not send ");
            }

        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue in asn_header");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

    }

    public static String getDataFromUserModelMapping() throws ClassNotFoundException, SQLException {

        String ModelName="";
        ResultSet resultSet = null;
        PreparedStatement preparedStatementTest = null;
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

        System.out.println("Connection success for UserModelMapping");
        Statement stat = conn.createStatement();


        String dataForUserModelMapping = DBConstant.queryForUserModelMapping;
        preparedStatementTest = conn.prepareStatement(dataForUserModelMapping);
        // Set parameters (if needed)
        preparedStatementTest.setString(1, userName);

        resultSet = preparedStatementTest.executeQuery();


        try {
            // Process the ResultSet
            while (resultSet.next()) {
                ModelName = resultSet.getString(3);
                System.out.println(" Model Name from userModel mapping--> " + ModelName);
            }
        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue in INVA generation");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return ModelName;

    }
    public static Map<String, String> getDataFromAttInputTestData(int offsetcount) throws ClassNotFoundException, SQLException {

        String queryForATTInputTestData="select  * from qa_automation_testing.attInputTestData where model = ? order by id asc limit 1 offset "+offsetcount;

        // used in the query after limit 1 ---> offset "+offsetcount
        System.out.println("queryForATTInputTestData "+ queryForATTInputTestData);
        Map<String,String> mapForATTInputData=new HashMap<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatementTest = null;
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL,
                DBConstant.STRING_DB_PSQL_USERNAME, DBConstant.STRING_DB_PSQL_PASSWORD);

        System.out.println("Connection success");
        Statement stat = conn.createStatement();


        String queryToGetAttInputTestData = queryForATTInputTestData;
        preparedStatementTest = conn.prepareStatement(queryToGetAttInputTestData);
        preparedStatementTest.setString(1,getDataFromUserModelMapping() );

        resultSet = preparedStatementTest.executeQuery();
        // resultSet=stmt.executeQuery(queryToGetDataForVisualInspection);

        ResultSetMetaData rsmd = resultSet.getMetaData();

        int columnsNumber = rsmd.getColumnCount();
        String columnName="";
        String columnValue="";
        try {
            while (resultSet.next()) {


                for (int i = 1; i <= columnsNumber; i++) {
                    columnName = rsmd.getColumnName(i);
                    columnValue = resultSet.getString(i);
                    mapForATTInputData.put(columnName, columnValue);
                    System.out.println(mapForATTInputData.get(columnName));

                }

                // System.out.println("pcncheck" + mapForATTInputData.get("pcncheck"));


//                resultOfVIStep = resultSet.getString(2);
//                modelFromRuleDB = resultSet.getString(5);
//                nextStepOnVI = resultSet.getString(7);
//
//                nextSteps.add(nextStepOnVI);
//
//                System.out.println("resultOfStep - "+ resultOfVIStep + ",modelFromRuleDB - "+modelFromRuleDB+", nextStepOnVI - "+nextStepOnVI);

            }
            System.out.println("mapForATTInputData " +mapForATTInputData);
        }
        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue in AttInputTestData");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return mapForATTInputData;
    }

    public static String getMasterCartonId(String serialNumber) throws ClassNotFoundException, SQLException {

        String masterCartonId="";

        ResultSet resultSet = null;
        PreparedStatement preparedStatementTest = null;
        Class.forName(DBConstant.STRING_DB_PSQL_DRIVER);
        conn = DriverManager.getConnection(DBConstant.STRING_DB_PSQL_URL_UAT,
                DBConstant.STRING_DB_PSQL_UAT_USERNAME, DBConstant.STRING_DB_PSQL_UAT_PASSWORD);

        System.out.println("Connection success");
        Statement stat = conn.createStatement();


        String queryToGetAttInputTestData = DBConstant.queryTogetMasterCartonId;
        preparedStatementTest = conn.prepareStatement(queryToGetAttInputTestData);
        preparedStatementTest.setString(1,serialNumber);

        resultSet = preparedStatementTest.executeQuery();
        // resultSet=stmt.executeQuery(queryToGetDataForVisualInspection);

        try {
            while (resultSet.next()) {


                masterCartonId=resultSet.getString(2);
                System.out.println("masterCartonId "+masterCartonId);

                }
            }


        catch (SQLException e) {
            // Handle the exception
            System.out.println("there might some issue in getMasterCartonId");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return masterCartonId;

    }





    // Convert password into Base64
    private static String encodeToBase64(String input) throws UnsupportedEncodingException {
        // Encode to Base64
        String  encodeString = Base64.getEncoder().encodeToString(input.getBytes("UTF-8"));

        System.out.println("encodeString "+encodeString);
        // Convert the byte array to a String
        return new String(encodeString);
    }
}
