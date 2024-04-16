package utilityPackage;

import java.util.Arrays;
import java.util.List;

public class DBConstant {
    public final static String STRING_DB_PSQL_DRIVER="org.postgresql.Driver";
    public final static String STRING_DB_PSQL_URL="jdbc:postgresql://172.10.0.232:5432/attqaautomation";
    public final static String STRING_DB_PSQL_URL_receving="jdbc:postgresql://172.10.0.232:5432/mp_core_receiving";
    public final static String STRING_DB_PSQL_USERNAME="postgres";
    public final static String STRING_DB_PSQL_PASSWORD="zxcvbnm@123";

    // UAT Credentials

//    public final static String STRING_DB_PSQL_URL_UAT ="jdbc:postgresql://172.10.0.240:5432/mp_core_receiving";
//    public final static String STRING_DB_PSQL_UAT_USERNAME="encore_readonly";
//    public final static String STRING_DB_PSQL_UAT_PASSWORD="CNLwuVuN7Lj3VvQukhm2M";

//    public final static String STRING_DB_PSQL_URL_UAT ="jdbc:postgresql://34.234.9.191:5432/mp_core_receiving?sslmode=disable";
//    public final static String STRING_DB_PSQL_UAT_USERNAME="db_admin_user";
//    public final static String STRING_DB_PSQL_UAT_PASSWORD="1BuO2024kqOD";

    public final static String STRING_DB_PSQL_URL_UAT ="jdbc:postgresql://midas-pg-pgsql-preprod.cgsvwuzlc3v3.us-east-1.rds.amazonaws.com:5432/mp_core_receiving";
    public final static String STRING_DB_PSQL_UAT_USERNAME="postgres";
    public final static String STRING_DB_PSQL_UAT_PASSWORD="WDLcnRqsQBSwhre34w6u";

    public final static String queryToGetSerialNumberList="select loadid,sku,sourcemodel,sourceserialnumber,nextstep,* from public.tblreceivingtransaction t where loadid in\n" +
            "('104092') and nextstep='VISUAL_INSPECTION'";

    public final static String queryForProfileMaster="select * from public.profile_master pm where org_id =? and sku=?";

    public final static String queryToGetpalletId="select * from receiving.tblcartonpalletmapping where cartonid = ?";

    public final static String queryForRMARValidation ="select count(*) from stg.tbl_rmar_receipts_stg trrs where serial_number = ? ";

    public final static String queryForINVAValidation ="SELECT * FROM stg.tbl_inva_outbound_stg WHERE serial_number = ? ORDER BY inva_id desc LIMIT 1;";

    public final static String queryForASN="select * from stg.asn_header ah where asn_header_asnnumber = ?";

    public final static String queryForUserModelMapping="select * from qa_automation_testing.user_model_mapping where user_name = ? and is_enabled =true and is_deleted =false";

    public final static String queryTogetMasterCartonId="select * from receiving.tbldevicecartonmapping t where receivedserialnumber  =? ";



//    public final static String queryToGetSerialNumberList="select loadid,sku,sourcemodel,sourceserialnumber,nextstep,* from public.tblreceivingtransaction t where loadid in\n" +
//            "('222997') and nextstep='VISUAL_INSPECTION'";

    // 210 -222997    ,320-500 -223007 , 320-505-221988

    // public final static String Query_For_testcase_master="select * from qa_automation_testing.testcase_master tm where org_id =? and testcase_id in ("+builder.deleteCharAt( builder.length() -1 ).toString()+");";

    // public final static String queryForVisualInspection="select * from qa_automation_testing.tblvisualinspection t limit 1";

    //public final static String queryForVisualInspection="SELECT * FROM ? t ORDER BY \"case\" DESC LIMIT 1 OFFSET 1";

    public final static String Query_For_org_master ="select * from qa_automation_testing.org_master om where org_id=?";


    public final static String queryForQuestionnairesList="select\n" +
            "\tdistinct a.questionid,\n" +
            "\ta.questioncode,\n" +
            "\ta.questiontext,\n" +
            "\ta.questionsequencenumber,\n" +
            "\ta.answertypeid,\n" +
            "\td.answertypecode\n" +
            "from \n" +
            "\treceiving.tblrcvquestionnaire a\n" +
            "inner join receiving.tblrcvquestionnairechannelorgassoc b on\n" +
            "\ta.questionid = b.questionid\n" +
            "inner join receiving.tblrcvquestionnairecategory c on\n" +
            "\ta.categoryid = c.categoryid \n" +
            "inner join receiving.tblrcvquestionnaireanswertype d on\n" +
            "\ta.answertypeid = d.answertypeid\n" +
            "where\n" +
            "\tb.orgid=?\n" +
            "\tand c.categoryname = ? and a.enabledflag =true;";

    /*public final static String queryForQuestionnairesList="select\n" +
            "\tdistinct a.questionid,\n" +
            "\ta.questioncode,\n" +
            "\ta.questiontext,\n" +
            "\ta.questionsequencenumber,\n" +
            "\ta.enabledflag,\n" +
            "\ta.categoryid,\n" +
            "\ta.answertypeid,\n" +
            "\td.answertypecode,\n" +
            "\ta.updateddate,\n" +
            "\tc.categoryname\n" +
            "from\n" +
            "\treceiving.tblrcvquestionnaire a\n" +
            "inner join receiving.tblrcvquestionnairechannelorgassoc b on\n" +
            "\ta.questionid = b.questionid\n" +
            "inner join receiving.tblrcvquestionnairecategory c on\n" +
            "\ta.categoryid = c.categoryid\n" +
            "inner join receiving.tblrcvquestionnaireanswertype d on\n" +
            "\ta.answertypeid = d.answertypeid\n" +
            "where\n" +
            "\tb.orgid = ?\n" +
            "\tand c.categoryname = ?\n"+
            "order by\n" +
            "\tupdateddate asc ;";*/



    public static String Query_For_testcase_master = null;


//    static {
//        List<?> array = Arrays.asList(ReadDataFromPropertyFile.data.getProperty("TestCases").split(",")); // Assuming you have a list of testcase_ids
//        StringBuilder placeholdersBuilder = new StringBuilder();
//        for (Object TC : array) {
//            placeholdersBuilder.append("?, ");
//        }
//
//        // Remove the trailing comma and space if there's at least one placeholder
//        if (placeholdersBuilder.length() > 0) {
//            placeholdersBuilder.setLength(placeholdersBuilder.length() - 2);
//        }

//        StringBuilder builder = new StringBuilder(placeholdersBuilder);
//
//        Query_For_testcase_master = "select * from qa_automation_testing.testcase_master tm where org_id = ? and testcase_id in (" + builder.toString() + ");";
//        //Query_For_testcase_master = "select * from qa_automation_testing.testcase_master tm where org_id = ? and testcase_id in (" + ReadDataFromPropertyFile.data.getProperty("TestCases").toString() + ")";
//
//
//    }

    public final static String testCaseIdFromConfigMaster ="select * from qa_automation_testing.testcase_master where org_id=? and testcase_id= ?";
    public final static String queryForConfigurationMaster="select * from qa_automation_testing.configuration_master where config_id=?";
    public final static String queryForOrgMasterToFetchProviderData="select * from qa_automation_testing.org_master om WHERE org_id=?";
    public final static String fetchTestCaseWithSanityValue="select * from qa_automation_testing.testcase_master where org_id = ? and is_sanity=?";
    public final static String fetchTestCaseWithRegression= "select * from qa_automation_testing.testcase_master where org_id =?";

//    public static void main(String[] args) {
//
//        System.out.println(Query_For_testcase_master);
//        System.out.println(Arrays.asList(ReadDataFromPropertyFile.data.getProperty("TestCases").split(",")));
//
//
//    }
}
